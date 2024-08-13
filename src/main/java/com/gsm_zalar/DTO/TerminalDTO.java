package com.gsm_zalar.DTO;

import com.gsm_zalar.Models.Terminal;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TerminalDTO {
    private int id;
    private String marque;
    private String model;
    private String ram;
    private String rom;
    private String imei;
    private String grade;
    private String dateAcquisition;
    private Integer dureeGarantie;
    private Boolean affectation;
    private String dateRetour;
    private String  fournisseur;
    private String fillialeLibelle;
    private Boolean deleted;
    private String dateDeletion;
    private String deletedBy;
    public TerminalDTO(Terminal terminal){
        this.marque=terminal.getMarque();
        this.model=terminal.getModel();
        this.ram=terminal.getRam();
        this.imei=terminal.getImei();
        this.rom=terminal.getRom();
        this.affectation=terminal.getAffectation();
        this.dateAcquisition=terminal.getDateAcquisition();
        this.fournisseur=terminal.getFournisseur().getLibelle();
        this.fillialeLibelle=terminal.getFilliale().getLibelle();
    }

}
