package com.gsm_zalar.Controllers;

import com.gsm_zalar.DTO.FournisseurDTO;
import com.gsm_zalar.Services.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fournisseurs")
public class FournisseurController {

    @Autowired
    private FournisseurService fournisseurService;

    @PostMapping
    public ResponseEntity<FournisseurDTO> createFournisseur(@RequestBody FournisseurDTO fournisseurDTO) {
        FournisseurDTO createdFournisseur = fournisseurService.createFournisseur(fournisseurDTO);
        return new ResponseEntity<>(createdFournisseur, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FournisseurDTO> updateFournisseur(@PathVariable int id, @RequestBody FournisseurDTO fournisseurDTO) {
        FournisseurDTO updatedFournisseur = fournisseurService.updateFournisseur(id, fournisseurDTO);
        return new ResponseEntity<>(updatedFournisseur, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFournisseur(@PathVariable("id") int id, @RequestParam String email) {
        fournisseurService.deleteFournisseur(id,email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/recuperer/{id}")
    public ResponseEntity<Void> recupererFournisseur(@PathVariable("id") int id) {
        fournisseurService.recupererFournisseur(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    @GetMapping
    public ResponseEntity<List<FournisseurDTO>> getAllFournisseurs() {
        List<FournisseurDTO> fournisseurs = fournisseurService.getAllFournisseurs();
        return new ResponseEntity<>(fournisseurs, HttpStatus.OK);
    }
    @GetMapping("/Historique")
    public ResponseEntity<List<FournisseurDTO>> getHistorique() {
        List<FournisseurDTO> fournisseurs = fournisseurService.getHistorique();
        return new ResponseEntity<>(fournisseurs, HttpStatus.OK);
    }
}
