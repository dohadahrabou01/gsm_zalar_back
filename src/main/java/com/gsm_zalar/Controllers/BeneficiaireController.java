package com.gsm_zalar.Controllers;

import com.gsm_zalar.DTO.BeneficiareDTO;
import com.gsm_zalar.Models.Beneficiare;
import com.gsm_zalar.Repositories.BeneficiaireRepository;
import com.gsm_zalar.Services.BeneficiareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiares")
public class BeneficiaireController {

    @Autowired
    private BeneficiareService beneficiareService;
    @Autowired
    private BeneficiaireRepository beneficiaireRepository;
    @PostMapping("/ADD")
    public ResponseEntity<BeneficiareDTO> createBeneficiare(@RequestBody BeneficiareDTO beneficiareDTO) {
        BeneficiareDTO createdBeneficiare = beneficiareService.createBeneficiare(beneficiareDTO);
        return ResponseEntity.ok(createdBeneficiare);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BeneficiareDTO> updateBeneficiare(@PathVariable int id, @RequestBody BeneficiareDTO beneficiareDTO) {
        BeneficiareDTO updatedBeneficiare = beneficiareService.updateBeneficiare(id, beneficiareDTO);
        return ResponseEntity.ok(updatedBeneficiare);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a valid file");
        }

        System.out.println("Received file: " + file.getOriginalFilename()); // Debug line

        try {
            List<Beneficiare> beneficiaires = beneficiareService.importFromExcel(file);
            beneficiaireRepository.saveAll(beneficiaires);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/beneficiaires/download/")
                    .path(file.getOriginalFilename())
                    .toUriString();

            return ResponseEntity.ok("File uploaded successfully: " + fileDownloadUri);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<BeneficiareDTO> getBeneficiareById(@PathVariable int id) {
        BeneficiareDTO beneficiareDTO = beneficiareService.getBeneficiareById(id);
        return ResponseEntity.ok(beneficiareDTO);
    }
    @GetMapping("/FillialesLibelle/")
    public ResponseEntity<Integer> getBeneficiareById(@RequestParam String  Libelle) {
        int count = beneficiareService.countBeneficiaire(Libelle);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/Beneficiaires")
    public ResponseEntity<List<BeneficiareDTO>> getAllBeneficiares() {
        List<BeneficiareDTO> beneficiares = beneficiareService.getAllBeneficiares();
        return ResponseEntity.ok(beneficiares);
    }
    @GetMapping("/Historique")
    public ResponseEntity<List<BeneficiareDTO>> getHistorique() {
        List<BeneficiareDTO> beneficiares = beneficiareService.getHistorique();
        return ResponseEntity.ok(beneficiares);
    }
    @GetMapping("/ByFilliale")
    public ResponseEntity<List<BeneficiareDTO>> getByFilliale(String email) {
        List<BeneficiareDTO> beneficiares = beneficiareService.getByFilliales(email);
        return ResponseEntity.ok(beneficiares);
    }
    @GetMapping("/ByEmail")
    public ResponseEntity<List<BeneficiareDTO>> getByEmail(String email) {
        List<BeneficiareDTO> beneficiares = beneficiareService.getByEmail(email);
        return ResponseEntity.ok(beneficiares);
    }
    @GetMapping("/by-numero")
    public ResponseEntity<List<BeneficiareDTO>> getBeneficiaresByNumero(@RequestParam String numero) {
        try {
            List<BeneficiareDTO> beneficiares = beneficiareService.getByNumero(numero);
            return ResponseEntity.ok(beneficiares);
        } catch (Exception e) {
            // Gérer les exceptions ou erreurs ici, par exemple, en renvoyant une erreur HTTP 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/by-imei")
    public ResponseEntity<List<BeneficiareDTO>> getBeneficiaresByTerminal(@RequestParam String imei) {
        try {
            List<BeneficiareDTO> beneficiares = beneficiareService.getByTerminal(imei );
            return ResponseEntity.ok(beneficiares);
        } catch (Exception e) {
            // Gérer les exceptions ou erreurs ici, par exemple, en renvoyant une erreur HTTP 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeneficiare(@PathVariable int id, @RequestParam String email) {
        beneficiareService.deleteBeneficiare(id,email);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/recuperer/{id}")
    public ResponseEntity<Void> recupererBeneficiare(@PathVariable int id) {
        beneficiareService.recupererBeneficiare(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/by-code/{code}")
    public ResponseEntity<BeneficiareDTO> getBeneficiareByCode(@PathVariable String code) {
        BeneficiareDTO beneficiareDTO = beneficiareService.getByCode(code);
        return ResponseEntity.ok(beneficiareDTO);
    }
}
