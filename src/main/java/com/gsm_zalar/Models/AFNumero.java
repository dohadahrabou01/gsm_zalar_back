package com.gsm_zalar.Models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@DiscriminatorValue("AFNumero")  // Utilisation de @DiscriminatorValue
public class AFNumero extends Affectation {

    @ManyToOne
    @JoinColumn(name = "numero_id")
    private Numero numero;

      }
