package com.gsm_zalar.Services;

import com.gsm_zalar.DTO.JwtAuthenticationResponse;
import com.gsm_zalar.DTO.SigninRequest;

public interface AuthService {
    public JwtAuthenticationResponse signin(SigninRequest signinRequest);
}
