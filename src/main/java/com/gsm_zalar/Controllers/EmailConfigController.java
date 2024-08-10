package com.gsm_zalar.Controllers;

import com.gsm_zalar.Models.EmailConfig;
import com.gsm_zalar.Services.EmailConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email-config")
public class EmailConfigController {

    @Autowired
    private EmailConfigService emailConfigService;

    @GetMapping
    public EmailConfig getEmailConfig() {
        return emailConfigService.getEmailConfig();
    }

    @PutMapping
    public void updateEmailConfig(@RequestBody EmailConfig emailConfig) {
        emailConfigService.updateEmailConfig(emailConfig);
    }
}
