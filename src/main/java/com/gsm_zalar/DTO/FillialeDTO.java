package com.gsm_zalar.DTO;

import lombok.Data;

@Data
public class FillialeDTO {
    private int id;
    private String libelle;
    private String lieu;
    private String img;

    public FillialeDTO(int id, String libelle, String lieu,String img) {
        this.id=id;
        this.libelle=libelle;
        this.lieu=lieu;
        this.img=img;
    }

    public FillialeDTO() {

    }

    public void setGerant(Object o) {
    }
}