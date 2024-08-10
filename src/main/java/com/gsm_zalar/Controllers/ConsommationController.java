package com.gsm_zalar.Controllers;

import com.gsm_zalar.DTO.ConsommationDTO;
import com.gsm_zalar.Services.ConsommationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consommations")
public class ConsommationController {

    @Autowired
    private ConsommationService consommationService;

    @PostMapping
    public ResponseEntity<ConsommationDTO> createConsommation(@RequestBody ConsommationDTO consommationDTO) {
        ConsommationDTO createdConsommation = consommationService.createConsommation(consommationDTO);
        return new ResponseEntity<>(createdConsommation, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ConsommationDTO>> getAllConsommations() {
        List<ConsommationDTO> consommations = consommationService.getAllConsommations();
        return new ResponseEntity<>(consommations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsommationDTO> getConsommationById(@PathVariable int id) {
        try {
            ConsommationDTO consommation = consommationService.getConsommationById(id);
            return new ResponseEntity<>(consommation, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ConsommationDTO> updateConsommation(
            @PathVariable int id,
            @RequestBody ConsommationDTO consommationDTO) {
        try {
            ConsommationDTO updatedConsommation = consommationService.updateConsommation(id, consommationDTO);
            return new ResponseEntity<>(updatedConsommation, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsommation(@PathVariable int id) {
        try {
            consommationService.deleteConsommation(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
