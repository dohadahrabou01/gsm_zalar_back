package com.gsm_zalar.Repositories;

import com.gsm_zalar.Models.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Integer> {
   Optional<PasswordResetToken> findByToken(String token);
}
