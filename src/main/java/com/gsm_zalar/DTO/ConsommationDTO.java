package com.gsm_zalar.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConsommationDTO {
    private int id;
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
    private String numero;
    private String  annee;
}
