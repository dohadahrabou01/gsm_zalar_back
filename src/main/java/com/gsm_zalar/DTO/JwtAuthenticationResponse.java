package com.gsm_zalar.DTO;

import com.gsm_zalar.Models.Role;
import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    private String token;
    private String refreshToken;
    private String email;
    private String role;



    public JwtAuthenticationResponse() {

    }
}
