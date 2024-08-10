package com.gsm_zalar.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="saveduser")
public class PendingUserUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private String newEmail;
    private String newPassword;
    private String newRole;
    private String nom;
    private String prenom;
    private LocalDateTime createdAt;

    // Getters and setters
}
