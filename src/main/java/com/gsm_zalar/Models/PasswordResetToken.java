package com.gsm_zalar.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@Entity
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String token;
   public String getToken(){
       return token;
   }
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Date expiryDate;

    // Getters et Setters
}
