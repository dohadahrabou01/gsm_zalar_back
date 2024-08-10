package com.gsm_zalar.Controllers;

import com.gsm_zalar.DTO.JwtAuthenticationResponse;
import com.gsm_zalar.DTO.SigninRequest;

import com.gsm_zalar.Models.PasswordResetRequest;
import com.gsm_zalar.Services.AuthService;
import com.gsm_zalar.Services.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthControlleur {
    private final AuthService authenticationService;

    @Autowired
    private PasswordResetService passwordResetService;


    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest signinRequest) {
        JwtAuthenticationResponse response = authenticationService.signin(signinRequest);
        return ResponseEntity.ok(response);
    }



    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        passwordResetService.requestPasswordReset(email);
        return ResponseEntity.ok("Un e-mail a été envoyé si cet e-mail est associé à un compte.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
        passwordResetService.updatePassword(request);
        return ResponseEntity.ok("Password reset successfully");
    }
}
