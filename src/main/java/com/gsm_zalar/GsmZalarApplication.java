package com.gsm_zalar;

import com.gsm_zalar.Models.Role;
import com.gsm_zalar.Models.User;
import com.gsm_zalar.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@AllArgsConstructor
public class GsmZalarApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        SpringApplication.run(GsmZalarApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User user=userRepository.findByRole(Role.ADMIN);
       if(user==null || user.getDeleted()){
           User user1=new User();
           user1.setNom("SUPER");
           user1.setPrenom("ADMIN");
           user1.setEmail("doha.dahrabou@gmail.com");
           user1.setRole(Role.ADMIN);
           String encryptedPassword = passwordEncoder.encode("ADMIN2024");
           user1.setPassword(encryptedPassword);
           userRepository.save(user1);
       }

    }
}
