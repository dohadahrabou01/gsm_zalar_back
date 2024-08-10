package com.gsm_zalar.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "filliales")
public class Filliale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String libelle; // Consider renaming
    private String lieu; // Consider renaming

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private User responsable;

    @OneToOne
    @JoinColumn(name = "gerant_id")
    private User gerant;


    @OneToMany(mappedBy = "filliale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Beneficiare> beneficiares;
    @OneToMany(mappedBy = "fillialenumero", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Numero> Numeros;
    @OneToMany(mappedBy = "fillialeterminale")
    private Collection<Terminal> terminales;
    private String imagePath;

    private Boolean isDeleted = false; // Consider renaming

    private String dateDeletion; // Use LocalDateTime

    private String deletedBy; // Use camelCase

    public void setResponsable(User responsable) {
        this.responsable = responsable;
    }


}
