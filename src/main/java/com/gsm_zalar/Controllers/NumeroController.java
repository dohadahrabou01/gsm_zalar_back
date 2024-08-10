package com.gsm_zalar.Controllers;

import com.gsm_zalar.DTO.NumeroDTO;
import com.gsm_zalar.Models.Numero;
import com.gsm_zalar.Repositories.NumeroRepository;
import com.gsm_zalar.Services.NumeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/numeros")
public class NumeroController {

    @Autowired
    private NumeroService numeroService;
    @Autowired
    private NumeroRepository numeroRepository;

    @PostMapping
    public ResponseEntity<NumeroDTO> createNumero(@RequestBody NumeroDTO numeroDTO) {
        NumeroDTO createdNumero = numeroService.createNumero(numeroDTO);
        return new ResponseEntity<>(createdNumero, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NumeroDTO>> getAllNumeros() {
        List<NumeroDTO> numeros = numeroService.getAllNumeros();
        return new ResponseEntity<>(numeros, HttpStatus.OK);
    }
    @GetMapping("/Historique")
    public ResponseEntity<List<NumeroDTO>> getHistorique() {
        List<NumeroDTO> numeros = numeroService.getHistorique();
        return new ResponseEntity<>(numeros, HttpStatus.OK);
    }
    @GetMapping("/pourcentage-affectation")
    public double obtenirPourcentageAffectation() {
        return numeroService.calculerPourcentageAffectation();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NumeroDTO> getNumeroById(@PathVariable("id") int id) {
        NumeroDTO numero = numeroService.getNumeroById(id);
        if (numero != null) {
            return new ResponseEntity<>(numero, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<NumeroDTO> updateNumero(@PathVariable("id") int id, @RequestBody NumeroDTO numeroDTO) {
        NumeroDTO updatedNumero = numeroService.updateNumero(id, numeroDTO);
        if (updatedNumero != null) {
            return new ResponseEntity<>(updatedNumero, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/by-code/{code}")
    public ResponseEntity<List<NumeroDTO>> getNumerosByCode(@PathVariable String code) {
        List<NumeroDTO> numeroDTOList = numeroService.getNumeroByCode(code);
        return ResponseEntity.ok(numeroDTOList);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNumero(@PathVariable("id") int id, @RequestParam String email) {
        numeroService.deleteNumero(id, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/recuperer/{id}")
    public ResponseEntity<Void> recupererNumero(@PathVariable("id") int id) {
        numeroService.recupererNumero(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/by-filliales")
    public ResponseEntity<List<NumeroDTO>> getByFilliales(@RequestParam String email) {
        List<NumeroDTO> numeros = numeroService.getByFilliales(email);
        return new ResponseEntity<>(numeros, HttpStatus.OK);
    }
    @GetMapping("/ByEmail")
    public ResponseEntity<List<NumeroDTO>> getByEmail(String email) {
        List<NumeroDTO> numeros = numeroService.getByEmail(email);
        return new ResponseEntity<>(numeros, HttpStatus.OK);
    }
    @PostMapping("/import")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a valid file");
        }

        System.out.println("Received file: " + file.getOriginalFilename()); // Debug line

        try {
            List<Numero> numeros = numeroService.importFromExcel(file);
            numeroRepository.saveAll(numeros);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/numeros/download/")
                    .path(file.getOriginalFilename())
                    .toUriString();

            return ResponseEntity.ok("File uploaded successfully: " + fileDownloadUri);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        }
    }
}
