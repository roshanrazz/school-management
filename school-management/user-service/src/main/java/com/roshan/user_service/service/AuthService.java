package com.roshan.user_service.service;


import com.roshan.user_service.dto.LoginDto;
import com.roshan.user_service.dto.RefreshTokenDto;
import com.roshan.user_service.dto.TokenResponseDto;

public interface AuthService {

    TokenResponseDto login(LoginDto loginDto);

    TokenResponseDto refreshToken(RefreshTokenDto refreshToken);
}
