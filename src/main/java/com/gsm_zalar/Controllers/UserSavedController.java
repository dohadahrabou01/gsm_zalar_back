package com.gsm_zalar.Controllers;

import com.gsm_zalar.Models.PendingUserUpdate;
import com.gsm_zalar.Models.Role;
import com.gsm_zalar.Models.User;
import com.gsm_zalar.Repositories.PendingUserUpdateRepository;
import com.gsm_zalar.Repositories.UserRepository;
import com.gsm_zalar.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/validate")
public class UserSavedController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PendingUserUpdateRepository pendingUserUpdateRepository;
    @PutMapping("/{updateId}")
    public ResponseEntity<String> validateUpdate(@PathVariable Long updateId) {
        Optional<PendingUserUpdate> optionalPendingUpdate = pendingUserUpdateRepository.findByIdAndCreatedAtAfter(updateId, LocalDateTime.now().minusHours(24));

        if (!optionalPendingUpdate.isPresent()) {
            return ResponseEntity.status(HttpStatus.GONE).body("Link has expired or is invalid");
        }

        PendingUserUpdate pendingUpdate = optionalPendingUpdate.get();
        User existingUser = userRepository.findById(pendingUpdate.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + pendingUpdate.getUserId()));

        existingUser.setEmail(pendingUpdate.getNewEmail());
        existingUser.setNom(pendingUpdate.getNom());
        existingUser.setPrenom(pendingUpdate.getPrenom());
        if (pendingUpdate.getNewPassword() != null) {
            existingUser.setPassword(pendingUpdate.getNewPassword());
        }
        existingUser.setRole(Role.valueOf(pendingUpdate.getNewRole()));
        existingUser.setSessionId(UUID.randomUUID().toString());
        userRepository.save(existingUser);
        pendingUserUpdateRepository.delete(pendingUpdate);

        return ResponseEntity.ok("User update validated and applied successfully");
    }
}
