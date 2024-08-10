package com.gsm_zalar.Services;

import com.gsm_zalar.Models.PasswordResetRequest;
import com.gsm_zalar.Models.PasswordResetToken;
import com.gsm_zalar.Models.User;
import com.gsm_zalar.Repositories.PasswordResetTokenRepository;
import com.gsm_zalar.Repositories.UserRepository;
import com.gsm_zalar.Util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private EmailUtil emailUtil; // Utilisation de la classe EmailUtil

    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setToken(token);
            passwordResetToken.setUser(user);
            passwordResetToken.setExpiryDate(new Date(System.currentTimeMillis() + 3600000)); // 1 heure
            tokenRepository.save(passwordResetToken);

            // Construire le lien de réinitialisation
            String resetUrl = "http://localhost:3000/reset?token=" + token;;
            String subject = "Réinitialisation de votre mot de passe";
            String body = "Veuillez cliquer sur le lien suivant pour réinitialiser votre mot de passe : " + resetUrl;

            // Envoyer l'e-mail
            emailUtil.sendEmail(user.getEmail(), subject, body);
        }
    }
    public void updatePassword(PasswordResetRequest request) {
        Optional<PasswordResetToken> token = tokenRepository.findByToken(request.getToken());


        User user = token.get().getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Supprimer le token après utilisation
        tokenRepository.delete(token.get());
    }
}
