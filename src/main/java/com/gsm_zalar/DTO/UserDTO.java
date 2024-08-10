package com.gsm_zalar.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String role;
    private List<FillialeDTO> filliales;
    private FillialeDTO gerantFilliale;

    public UserDTO() {}

    public UserDTO(int id, String nom, String prenom, String email, String role,
                   List<FillialeDTO> filliales, FillialeDTO gerantFilliale) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
        this.filliales = filliales;
        this.gerantFilliale = gerantFilliale;
    }
    public CharSequence getPassword() {
        return password;
    }

    public FillialeDTO getFillialeGerant() {
        return gerantFilliale;
    }
}