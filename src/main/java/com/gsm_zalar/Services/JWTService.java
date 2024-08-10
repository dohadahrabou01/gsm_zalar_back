package com.gsm_zalar.Services;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JWTService {

    String extractUserName(String username);
    String generateToken(UserDetails userDetails);
    Boolean isTokenValid(String token,UserDetails userDetails);
    String generateRefreshToken(Map<String,Object> extraClaims, UserDetails userDetails);
}
