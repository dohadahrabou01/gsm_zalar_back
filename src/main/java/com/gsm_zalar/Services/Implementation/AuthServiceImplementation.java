package com.gsm_zalar.Services.Implementation;


import com.gsm_zalar.DTO.JwtAuthenticationResponse;
import com.gsm_zalar.DTO.SigninRequest;
import com.gsm_zalar.Models.User;
import com.gsm_zalar.Repositories.UserRepository;
import com.gsm_zalar.Services.AuthService;
import com.gsm_zalar.Services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signin(SigninRequest signinRequest) {
        // Authentifier l'utilisateur
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword())
        );

        // Trouver l'utilisateur par email
        User user = userRepository.findByEmail(signinRequest.getEmail());

        // Vérifier si l'utilisateur existe et s'il n'est pas supprimé
        if (user == null || user.isDeleted()) { // Supposons que `isDeleted()` renvoie true si l'utilisateur est marqué comme supprimé
            throw new UsernameNotFoundException("Utilisateur non trouvé ou supprimé");
        }

        // Générer les tokens
        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        // Préparer la réponse
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        jwtAuthenticationResponse.setEmail(signinRequest.getEmail());
        jwtAuthenticationResponse.setRole(user.getRole().name());

        return jwtAuthenticationResponse;
    }


}
