package com.gsm_zalar.DTO;

import com.gsm_zalar.Models.Beneficiare;
import com.gsm_zalar.Models.Terminal;
import lombok.Data;

@Data
public class AFTerminalDTO {
    int id;
    BeneficiareDTO beneficiareDTO;
    TerminalDTO terminalDTO;
    private String affectant;
    private String validation;

    private String date_affectation;
    private Boolean deleted ;
    private String date_deletion;
    private String deleted_by;
    private Boolean imprim;
    private int duree_max;




    public int getId() {
        return id;
    }

    public String getAffectant() {
        return affectant;
    }

    public void setAffectant(String affectant) {
        this.affectant=affectant;
    }

    public void setBeneficiareDTO(Beneficiare beneficiare) {
       BeneficiareDTO beneficiareDTO1=new BeneficiareDTO(beneficiare);
       this.beneficiareDTO=beneficiareDTO1;
    }
    public void setBeneficiareDTO(BeneficiareDTO beneficiare) {
        BeneficiareDTO beneficiareDTO1=new BeneficiareDTO(beneficiare);
        this.beneficiareDTO=beneficiareDTO1;
    }
    public void setTerminalDTO(Terminal terminal) {
        TerminalDTO terminalDTO1=new TerminalDTO(terminal);
        this.terminalDTO=terminalDTO1;
    }

    public void setDate_Affectation(String dateAffectation) {
        this.date_affectation=dateAffectation;
    }


}
