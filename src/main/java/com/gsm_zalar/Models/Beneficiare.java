package com.gsm_zalar.Models;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "beneficiaires")
public class Beneficiare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    private String prenom;
    private Grade grade;
    private String code;
    @Getter
    private Boolean multiple=false;
    @ManyToOne
    @JoinColumn(name = "filliale_id")
    private Filliale filliale;
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean deleted=false;
    private String date_deletion;
    private String deleted_by;
    public boolean isMultiple() {
        return multiple;
    }

    public String getDeleted_deletion() {
        return date_deletion;
    }
}
