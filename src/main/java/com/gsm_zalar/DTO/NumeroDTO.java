package com.gsm_zalar.DTO;

import com.gsm_zalar.DTO.ConsommationDTO;
import com.gsm_zalar.Models.Consommation;
import com.gsm_zalar.Models.Forfait;
import com.gsm_zalar.Models.Numero;
import com.gsm_zalar.Models.Operateur;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NumeroDTO {
    private int id;
    private String numero;
    private String serie;
    private String pin;
    private String puk;
    private Boolean affectation;
    private String operateur;
    private Boolean actif;
    private String forfaitLibelle;
    private String fillialeLibelle;

    public NumeroDTO(Numero numero) {
        this.forfaitLibelle=numero.getForfait().getLibelle();
        this.numero=numero.getNumero();
        this.serie=numero.getSerie();
        this.puk=numero.getPuk();
        this.pin=numero.getPin();
        this.affectation=numero.getAffectation();
        this.actif=numero.getActif();
        this.operateur=numero.getOperateur().name();
        this.fillialeLibelle=numero.getFilliale().getLibelle();



    }


    public String getForfait() {
        return forfaitLibelle;
    }

    public void setForfait(String byLibelle) {
        this.forfaitLibelle=byLibelle;
    }

    public Boolean isActif() {
        return actif;
    }
}
