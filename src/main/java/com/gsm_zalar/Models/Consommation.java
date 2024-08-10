package com.gsm_zalar.Models;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "consommations")
public class Consommation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "numero_id")
    private Numero numero;
    private double janvier;
    private double fevrier;
    private double mars;
    private double avril;
    private double mai;
    private double juin;
    private double juillet;
    private double aout;
    private double septembre;
    private double octobre;
    private double novembre;
    private double decembre;
    private String  annee;



}
