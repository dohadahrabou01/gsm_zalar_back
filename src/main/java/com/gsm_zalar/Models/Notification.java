package com.gsm_zalar.Models;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name="notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String message;
    @ManyToOne
    @JoinColumn(name = "afterminal_id")
   private AFTerminal afTerminal;

}
