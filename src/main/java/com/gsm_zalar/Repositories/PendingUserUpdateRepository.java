package com.gsm_zalar.Repositories;

import com.gsm_zalar.Models.PendingUserUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PendingUserUpdateRepository extends JpaRepository<PendingUserUpdate, Integer> {
    Optional<PendingUserUpdate> findByIdAndCreatedAtAfter(Long id, LocalDateTime time);
}