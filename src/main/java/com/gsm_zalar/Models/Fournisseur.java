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
@Table(name="fournisseurs")
public class  Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String libelle;
    private String tel;
    private String email;
    @OneToMany(mappedBy = "fournisseur")
    private List<Terminal> terminaux;
    private Boolean deleted=false;
    private String date_deletion;
    private String deleted_by;
}
