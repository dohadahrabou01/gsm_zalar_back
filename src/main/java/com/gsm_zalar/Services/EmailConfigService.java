package com.gsm_zalar.Services;

import com.gsm_zalar.Models.EmailConfig;
import com.gsm_zalar.Repositories.EmailConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailConfigService {

    @Autowired
    private EmailConfigRepository emailConfigRepository;

    public EmailConfig getEmailConfig() {
        return emailConfigRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Email configuration not found"));
    }

    public void updateEmailConfig(EmailConfig emailConfig) {
        emailConfig.setId(1); // Assuming there is only one configuration
        emailConfigRepository.save(emailConfig);
    }
}
