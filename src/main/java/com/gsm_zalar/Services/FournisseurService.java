package com.gsm_zalar.Services;

import com.gsm_zalar.DTO.FournisseurDTO;

import java.util.List;

public interface FournisseurService {
    FournisseurDTO createFournisseur(FournisseurDTO fournisseurDTO);
    FournisseurDTO updateFournisseur(int id, FournisseurDTO fournisseurDTO);
    void deleteFournisseur(int id,String email);

    List<FournisseurDTO> getAllFournisseurs();
    void recupererFournisseur(int id);
    List<FournisseurDTO> getHistorique();
}
