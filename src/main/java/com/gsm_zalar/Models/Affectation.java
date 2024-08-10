package com.gsm_zalar.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Table(name = "affectation")
public class Affectation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String date_affectation;

    @ManyToOne
    @JoinColumn(name = "affectant_id")
    private User affectant;

    @ManyToOne
    @JoinColumn(name = "beneficiare_id")
    private Beneficiare beneficiare;

    private Validation validation;

    private Boolean deleted = false;
    private String date_deletion;
    private String deleted_by;
    private Boolean imprim=false;

    private String ImageName;

    @Lob
    @Column(name = "imageData", columnDefinition="LONGBLOB")
    private byte[] imageData;

    public void setBeneficiare(Optional<Beneficiare> byId) {
        beneficiare=byId.get();
    }
    public String getDate_Affectation(){return date_affectation;}


    public void setBeneficiare(Beneficiare beneficiare) {
        this.beneficiare=beneficiare;
    }
}
