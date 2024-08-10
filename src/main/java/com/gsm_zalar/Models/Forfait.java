package com.gsm_zalar.Models;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "forfait")
public class Forfait {
    @Id
    @Generated@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String libelle;
    private String prix;
    @OneToMany(mappedBy = "forfait")
    private List<Numero> numeros;
    private Boolean deleted=false;
    private String date_deletion;
    private String deleted_by;

    public boolean isDeleted() {
        return deleted;
    }
}
