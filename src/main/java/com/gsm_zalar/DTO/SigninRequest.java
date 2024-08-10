package com.gsm_zalar.DTO;


import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class SigninRequest {
    private String email;
    private String password;
}
