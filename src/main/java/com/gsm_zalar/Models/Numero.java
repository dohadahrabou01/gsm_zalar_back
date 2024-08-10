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
@Table(name="numeros")
public class  Numero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String numero;
    private String serie;
    private String pin;
    private String puk;
    private Boolean affectation=false;

    private Operateur operateur;
    private boolean actif=true;
    @ManyToOne
    @JoinColumn(name = "forfait_id")
    private Forfait forfait;
    @ManyToOne
    @JoinColumn(name = "fillialenumero_id")
    private Filliale fillialenumero;
    private Boolean deleted=false;
    private String date_deletion;
    private String deleted_by;
    @OneToMany(mappedBy = "numero")
    private List<Consommation> consommations;

    public void setFilliale(Filliale byLibelle) {
        this.fillialenumero=byLibelle;
    }

    public Filliale getFilliale() {
        return fillialenumero;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Object orElseThrow() {
        return null;
    }

    public Boolean getActif() {
        return actif;
    }

    public Boolean isActif() {
        return actif;
    }
}
