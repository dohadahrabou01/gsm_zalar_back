package com.gsm_zalar.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EmailConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String host;
    private int port;
    private String username;
    private String password;

    // No need for explicit getters and setters
}
