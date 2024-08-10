package com.gsm_zalar.Repositories;

import com.gsm_zalar.Models.EmailConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailConfigRepository extends JpaRepository<EmailConfig, Integer> {
}