package com.gsm_zalar.Util;

import com.gsm_zalar.Models.EmailConfig;
import com.gsm_zalar.Repositories.EmailConfigRepository;
import com.gsm_zalar.Services.EmailConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

@Component
public class EmailUtil {

    @Autowired
    private EmailConfigService emailConfigService;

    private JavaMailSender mailSender;
    @Autowired
    private  EmailConfigRepository emailConfigRepository;


    @PostConstruct
    public void init() {
        try {
            this.mailSender = createJavaMailSender();
        } catch (Exception e) {
            // Log the exception or handle it
            e.printStackTrace();
        }
    }

    private JavaMailSender createJavaMailSender() {
        List<EmailConfig> emailConfigs = emailConfigRepository.findAll();
        EmailConfig emailConfig;

        if (emailConfigs.isEmpty()) {
            // No existing configuration found, create a new one
            EmailConfig defaultConfig = new EmailConfig();
            defaultConfig.setId(1); // Ensure this ID is unique or managed correctly
            defaultConfig.setHost("smtp.gmail.com");
            defaultConfig.setPort(587);
            defaultConfig.setUsername("doha.dahrabou@gmail.com");
            defaultConfig.setPassword("lqgdaugymqcgykay");
            emailConfigRepository.save(defaultConfig);

            System.out.println("Default EmailConfig saved.");
        }
            // Use the existing configuration
            emailConfig = emailConfigs.get(0); // Assuming there is only one config or you handle multiple cases


        // Configure the JavaMailSender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfig.getHost());
        mailSender.setPort(emailConfig.getPort());
        mailSender.setUsername(emailConfig.getUsername());
        mailSender.setPassword(emailConfig.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }


    public void sendEmail(String to, String subject, String body) {
        if (mailSender == null) {
            throw new IllegalStateException("JavaMailSender is not initialized");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            // Log the exception or handle it
            e.printStackTrace();
        }
    }
}
