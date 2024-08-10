package com.gsm_zalar.DTO;

import com.gsm_zalar.Models.Beneficiare;
import com.gsm_zalar.Models.Numero;
import lombok.Data;

@Data
public class AFNumeroDTO {


    int id;
    BeneficiareDTO beneficiareDTO;
    NumeroDTO numeroDTO;
    private String affectantEmail;


    private String date_affectation;
    private Boolean deleted ;
    private String date_deletion;
    private String deleted_by;
    private Boolean imprim;

    public void setId(int id) {
        this.id=id;
    }




    public void setNumeroDTO(Numero numero) {

        NumeroDTO numeroDTO1= new NumeroDTO(numero);
        numeroDTO=numeroDTO1;
    }
    public void setBeneficiareDTO(Beneficiare beneficiare) {
        BeneficiareDTO beneficiareDTO1=new BeneficiareDTO(beneficiare);
        beneficiareDTO=beneficiareDTO1;

    }


}
