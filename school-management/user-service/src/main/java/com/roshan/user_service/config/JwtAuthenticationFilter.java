package com.roshan.user_service.config;

import com.roshan.user_service.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String username = null;
        String jwt = null;

        String authHeader = request.getHeader("Authorization");

      try{
          if (authHeader != null && authHeader.startsWith("Bearer ")) {
              jwt = authHeader.substring(7);
              username = jwtUtils.extractUsername(jwt);
          }

          if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && jwtUtils.isAccessToken(jwt)) {
              UserDetails userDetails = userDetailsService.loadUserByUsername(username);
              if (jwtUtils.validateToken(jwt, userDetails.getUsername())) {
                  UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                  authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                  SecurityContextHolder.getContext().setAuthentication(authToken);
              }
          }

      }catch (Exception e) {
          log.warn("Failed to set user authentication -> Message: {}", e.getMessage());
      }
        filterChain.doFilter(request, response);
    }
}
