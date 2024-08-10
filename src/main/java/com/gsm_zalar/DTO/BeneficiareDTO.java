package com.gsm_zalar.DTO;

import com.gsm_zalar.Models.Beneficiare;
import lombok.Data;

@Data
public class BeneficiareDTO {

    private int id;
    private String nom;
    private String prenom;
    private String grade;
    private String fillialeLibelle;
    private Boolean deleted;
    private String dateDeletion;
    private String deletedBy;
    private Boolean multiple;
    private String code;
    public BeneficiareDTO(Beneficiare beneficiare) {
        this.id = beneficiare.getId();
        this.nom = beneficiare.getNom();
        this.prenom = beneficiare.getPrenom();
        this.grade = beneficiare.getGrade().name();
        this.dateDeletion = beneficiare.getDeleted_deletion();
        this.deletedBy = beneficiare.getDeleted_by();
        this.fillialeLibelle = beneficiare.getFilliale().getLibelle();
        this.deleted = beneficiare.getDeleted();
        this.code=beneficiare.getCode();
    }
    public BeneficiareDTO(BeneficiareDTO beneficiare) {
        this.id = beneficiare.getId();
        this.nom = beneficiare.getNom();
        this.prenom = beneficiare.getPrenom();
        this.grade = beneficiare.getGrade();
        this.dateDeletion = beneficiare.getDateDeletion();
        this.deletedBy = beneficiare.getDeletedBy();
        this.fillialeLibelle = beneficiare.getFillialeLibelle();
        this.deleted = beneficiare.getDeleted();
        this.code=beneficiare.getCode();
    }
    public BeneficiareDTO(){
        this.nom ="None";
        this.prenom ="None";
        this.grade = "None";
        this.dateDeletion = "None";
        this.deletedBy = "None";
        this.fillialeLibelle = "None";

        this.code="None";

    }
    public String getPrenom(){
        return prenom;
    }
    public String getNom(){
        return nom;
    }
    // Ajoutez les getters et setters si nécessaire
}

