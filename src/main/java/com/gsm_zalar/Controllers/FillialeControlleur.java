package com.gsm_zalar.Controllers;

import com.gsm_zalar.DTO.FillialeDTO;
import com.gsm_zalar.Models.Filliale;
import com.gsm_zalar.Services.FileStorageService;
import com.gsm_zalar.Services.FillilaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/filliales")
public class FillialeControlleur{

    @Autowired
    private FillilaleService fillialeService;
    @Autowired
    private FileStorageService fileStorageService;
    // URL: GET /api/filliales
    @GetMapping("/filliales")
    public List<FillialeDTO> getAllFilliales() {
        return fillialeService.getAllFilliales();
    }
    @GetMapping("/Historique")
    public List<FillialeDTO> getAllHistorique() {
        return fillialeService.getAllHistorique();
    }
    @GetMapping("/RSI")
    public List<FillialeDTO> getRSIFilliales() {
        return fillialeService.getRSIFilliales();
    }
    @GetMapping("/SI")
    public List<FillialeDTO> getSIFilliales() {
        return fillialeService.getSIFilliales();
    }

    // URL: GET /api/filliales/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Filliale> getFillialeById(@PathVariable int id) {
        Filliale filliale = fillialeService.getFillialeById(id);
        return filliale != null ? ResponseEntity.ok(filliale) : ResponseEntity.notFound().build();
    }

    // URL: POST /api/filliales
    @PostMapping
    public ResponseEntity<Filliale> addFiliale(
            @RequestParam("libelle") String libelle,
            @RequestParam("lieu") String lieu,
            @RequestParam("image") MultipartFile image) throws IOException {

        String imagePath = null;
        try {
            imagePath = fileStorageService.storeFile(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Filliale filiale = new Filliale();
        filiale.setLibelle(libelle);
        filiale.setLieu(lieu);
        filiale.setImagePath(imagePath);

        Filliale savedFiliale = fillialeService.createFilliale(filiale);
        return ResponseEntity.ok(savedFiliale);
    }

    // URL: PUT /api/filliales/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Filliale> updateFilliale(
            @PathVariable int id,
            @RequestParam(value = "libelle", required = false) String libelle,
            @RequestParam(value = "lieu", required = false) String lieu,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        // Retrieve existing Filliale
        Filliale existingFilliale = fillialeService.getFillialeById(id);
        if (existingFilliale == null) {
            return ResponseEntity.notFound().build();
        }

        // Update details
        if (libelle != null) {
            existingFilliale.setLibelle(libelle);
        }
        if (lieu != null) {
            existingFilliale.setLieu(lieu);
        }

        // Update image if provided
        if (image != null && !image.isEmpty()) {
            // Store new image and update path
            String imagePath = fileStorageService.storeFile(image);
            existingFilliale.setImagePath(imagePath);
        }

        // Save updated Filliale
        Filliale updatedFilliale = fillialeService.updateFilliale(id, existingFilliale);
        return ResponseEntity.ok(updatedFilliale);
    }


    // URL: DELETE /api/filliales/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilliale(@PathVariable int id,@RequestParam String email) {
        fillialeService.deleteFilliale(id,email);
        return ResponseEntity.noContent().build();
    }
    @PutMapping ("recuperer/{id}")
    public ResponseEntity<Void> recupererFilliale(@PathVariable int id) {
        fillialeService.recuperer(id);
        return ResponseEntity.noContent().build();
    }
    // Injecter votre service

    @GetMapping("/by-email")
    public ResponseEntity<List<FillialeDTO>> getFillialesByEmail(@RequestParam String email) {
        List<FillialeDTO> filliales = fillialeService.getEmailFilliales(email);
        return ResponseEntity.ok(filliales);
    }
    @GetMapping("/by-rsi")
    public ResponseEntity<List<FillialeDTO>> getFillialesByRSI(@RequestParam String email) {
        List<FillialeDTO> filliales = fillialeService.getSIRSIFilliales(email);
        return ResponseEntity.ok(filliales);
    }
}
