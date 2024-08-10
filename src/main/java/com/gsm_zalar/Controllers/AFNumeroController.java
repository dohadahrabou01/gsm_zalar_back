package com.gsm_zalar.Controllers;

import com.gsm_zalar.DTO.AFNumeroDTO;
import com.gsm_zalar.Models.AFNumero;
import com.gsm_zalar.Repositories.AFNumeroRepository;
import com.gsm_zalar.Services.AFNumeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/afnumero")
public class AFNumeroController {

    @Autowired
    private AFNumeroService afNumeroService;
    @Autowired
    private AFNumeroRepository afNumeroRepository;
    @PostMapping("/create")
    public ResponseEntity<Boolean> createAFNumero(
            @RequestParam String numero,
            @RequestParam int beneficiareId,
            @RequestParam String affectantEmail) {
        AFNumero createdAFNumero = afNumeroService.createAFNumero(numero, beneficiareId, affectantEmail);
        if (createdAFNumero != null) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }
    @PostMapping("/upload/{id}")
    public ResponseEntity<String> uploadFile(@PathVariable int id,@RequestParam("file") MultipartFile file) throws IOException {
         afNumeroService.storeFile(id,file);
        return ResponseEntity.ok("File uploaded successfully");
    }
    @GetMapping("/preuve/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable int id) {
        return  afNumeroService.getImage(id)
                .map(attachement -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachement.getImageName() + "\"")
                        .body(attachement.getImageData()))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AFNumeroDTO> getAFNumeroById(@PathVariable int id) {
        AFNumeroDTO afNumeroDTO = afNumeroService.getAFNumeroById(id);
        return ResponseEntity.ok(afNumeroDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AFNumeroDTO>> getAllAFNumeroDTOs() {
        List<AFNumeroDTO> afNumeroDTOs = afNumeroService.getAllAFNumeroDTOs();
        return ResponseEntity.ok(afNumeroDTOs);
    }
    @GetMapping("/Historique")
    public ResponseEntity<List<AFNumeroDTO>> getHistorique() {
        List<AFNumeroDTO> afNumeroDTOs = afNumeroService.getHistorique();
        return ResponseEntity.ok(afNumeroDTOs);
    }
    @GetMapping("/ByEmail")
    public ResponseEntity<List<AFNumeroDTO>> getByEmail(String email) {
        List<AFNumeroDTO> afNumeroDTOs = afNumeroService.getByEmail(email);
        return ResponseEntity.ok(afNumeroDTOs);
    }
    @PutMapping("/Recuperer/{id}")
    public ResponseEntity<?> recupererAFNumero(
            @PathVariable int id) {

        try {
            afNumeroService.recupererAFNumero(id);
            return ResponseEntity.ok().body("AFNumero successfully recuperer");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAFNumero(
            @PathVariable int id,
            @RequestParam String email) {

        try {
            afNumeroService.deleteAFNumero(id, email);
            return ResponseEntity.ok().body("AFNumero successfully deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
