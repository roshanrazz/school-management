package com.roshan.user_service.service.impl;


import com.roshan.user_service.dto.LoginDto;
import com.roshan.user_service.dto.RefreshTokenDto;
import com.roshan.user_service.dto.TokenResponseDto;
import com.roshan.user_service.entity.RefreshToken;
import com.roshan.user_service.repository.RefreshTokenRepository;
import com.roshan.user_service.service.AuthService;
import com.roshan.user_service.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public TokenResponseDto login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getEmail());
        String accessToken = jwtUtils.generateToken(userDetails.getUsername(), userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername());

//        if token exists delete it and save new token -> single login session ( for multi browser session don't delete token )
        refreshTokenRepository.findByEmail(userDetails.getUsername()).ifPresent(refreshTokenRepository::delete);
        saveRefreshToken(userDetails, refreshToken);

        return tokenDto(accessToken, refreshToken);
    }

    @Override
    public TokenResponseDto refreshToken(RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenDto.getRefreshToken()).orElseThrow(() -> new RuntimeException("Refresh token not found"));
        if (!jwtUtils.validateToken(refreshToken.getRefreshToken(), refreshToken.getEmail())) {
            throw new ExpiredJwtException(null,null,"Refresh token expired");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(refreshToken.getEmail());
        String accessToken = jwtUtils.generateToken(refreshToken.getEmail(), userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        return tokenDto(accessToken, refreshToken.getRefreshToken());
    }

    TokenResponseDto tokenDto(String accessToken, String refreshToken) {
        TokenResponseDto tokenDto = new TokenResponseDto();
        tokenDto.setAccessToken(accessToken);
        tokenDto.setRefreshToken(refreshToken);
        return tokenDto;
    }

    void saveRefreshToken(UserDetails userDetails, String refreshToken) {
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setEmail(userDetails.getUsername());
        refreshTokenRepository.save(refreshTokenEntity);
    }
}
