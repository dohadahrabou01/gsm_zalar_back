package com.gsm_zalar.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "terminaux")
public class Terminal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Getter
    @Setter
    private String marque;
    @Getter
    @Setter
    private String model;
    @Getter
    @Setter
    private String ram;
    @Getter
    @Setter
    private String rom;
    @Getter
    @Setter
    @Column(unique = true)
    private String imei;
    @Getter
    @Setter
    private String dateAcquisition;
    @Getter
    @Setter
    private int dureeGarantie;

    private Boolean affectation=false;
    private Grade grade;
    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;
    @ManyToOne
    @JoinColumn(name = "filliale_id")
    private Filliale fillialeterminale; // Ensure this matches with the Filliale mapping

    private Boolean deleted=false;
    @Getter
    @Setter
    private String dateDeletion;
    @Getter
    @Setter
    private String deletedBy;
    public boolean isAffectation() {
        return affectation;
    }

    public void setAffectation(boolean affectation) {
        this.affectation = affectation;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setFillialeterminal(Filliale byLibelle) {
        this.fillialeterminale=byLibelle;
    }



    public Fournisseur getByFournisseur() {
        return fournisseur;
    }

    public Filliale getByFilliale() {
        return fillialeterminale;
    }

    public boolean getIsDeleted() {
        return deleted;
    }

    public Filliale getFilliale() {
        return fillialeterminale;
    }

    public void setDate_affectation(String format) {
    }


    public Optional<Terminal> orElse(Object o) {
        return null;
    }
}
