package com.gsm_zalar.Services.Implementation;

import com.gsm_zalar.DTO.FournisseurDTO;
import com.gsm_zalar.Models.Fournisseur;
import com.gsm_zalar.Repositories.FournisseurRepository;
import com.gsm_zalar.Services.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FournisseurServiceImplementation implements FournisseurService {

    @Autowired
    private FournisseurRepository fournisseurRepository;



    @Override
    public FournisseurDTO createFournisseur(FournisseurDTO fournisseurDTO) {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setLibelle(fournisseurDTO.getLibelle());
        fournisseur.setTel(fournisseurDTO.getTel());
        fournisseur.setEmail(fournisseurDTO.getEmail());
        fournisseurRepository.save(fournisseur);
        return fournisseurDTO;
    }

    @Override
    public FournisseurDTO updateFournisseur(int id, FournisseurDTO fournisseurDTO) {
        if (!fournisseurRepository.existsById(id)) {
            throw new RuntimeException("Fournisseur not found");
        }
        Optional<Fournisseur> fournisseur =fournisseurRepository.findById(id);
        Fournisseur fournisseur1=fournisseur.get();
        fournisseur1.setLibelle(fournisseurDTO.getLibelle());
        fournisseur1.setTel(fournisseurDTO.getTel());
        fournisseur1.setEmail(fournisseurDTO.getEmail());
        fournisseurRepository.save(fournisseur1);
        return fournisseurDTO;
    }

    @Override
    public void deleteFournisseur(int id,String email) {
        Optional<Fournisseur> fournisseur=fournisseurRepository.findById(id);
        fournisseur.get().setDeleted(true);
        fournisseurRepository.save(fournisseur.get());
    }
    @Override
    public void recupererFournisseur(int id) {
        Optional<Fournisseur> fournisseur=fournisseurRepository.findById(id);
        fournisseur.get().setDeleted(false);
        fournisseurRepository.save(fournisseur.get());
    }



    @Override
    public List<FournisseurDTO> getAllFournisseurs() {
       List<Fournisseur> fournisseurList= fournisseurRepository.findByDeletedFalse();
       List<FournisseurDTO> fournisseurDTOS=new ArrayList<>();
       for(Fournisseur fournisseur:fournisseurList){
           FournisseurDTO fournisseurDTO=new FournisseurDTO();
           fournisseurDTO.setId(fournisseur.getId());
           fournisseurDTO.setLibelle(fournisseur.getLibelle()!=null?fournisseur.getLibelle():"None");
           fournisseurDTO.setTel(fournisseur.getTel()!=null?fournisseur.getTel():"None");
           fournisseurDTO.setEmail(fournisseur.getEmail()!=null?fournisseur.getEmail():"None");
           fournisseurDTOS.add(fournisseurDTO);
       }
           return   fournisseurDTOS;
    }

    @Override
    public List<FournisseurDTO> getHistorique() {
        List<Fournisseur> fournisseurList= fournisseurRepository.findByDeletedTrue();
        List<FournisseurDTO> fournisseurDTOS=new ArrayList<>();
        for(Fournisseur fournisseur:fournisseurList){
            FournisseurDTO fournisseurDTO=new FournisseurDTO();
            fournisseurDTO.setId(fournisseur.getId());
            fournisseurDTO.setLibelle(fournisseur.getLibelle()!=null?fournisseur.getLibelle():"None");
            fournisseurDTO.setTel(fournisseur.getTel()!=null?fournisseur.getTel():"None");
            fournisseurDTO.setEmail(fournisseur.getEmail()!=null?fournisseur.getEmail():"None");
            fournisseurDTOS.add(fournisseurDTO);
        }
        return   fournisseurDTOS;
    }
}
