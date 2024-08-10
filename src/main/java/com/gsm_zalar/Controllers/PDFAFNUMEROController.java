package com.gsm_zalar.Controllers;

import com.gsm_zalar.Models.AFNumero;
import com.gsm_zalar.Repositories.AFNumeroRepository;
import com.gsm_zalar.Services.PDFAFNumero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@RestController
@RequestMapping("/api/pdfAFNumero")
public class PDFAFNUMEROController {

    private final PDFAFNumero pdfService;
    @Autowired
    AFNumeroRepository afNumeroRepository;

    public PDFAFNUMEROController(PDFAFNumero pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable int id) {
        // Générer le flux PDF
        ByteArrayInputStream bis = pdfService.generatePdf(id);

        // Récupérer les détails nécessaires pour le nom du fichier
        Optional<AFNumero> afNumeroOptional = afNumeroRepository.findById(id);
        if (afNumeroOptional.isEmpty()) {
            // Gérer le cas où l'ID n'est pas trouvé
            throw new RuntimeException("AFNumero with ID " + id + " not found");
        }
        AFNumero afNumero = afNumeroOptional.get();
        String beneficiare = afNumero.getBeneficiare().getNom();
        String dateAffectation = afNumero.getDate_Affectation(); // Assurez-vous que cette date est formatée correctement

        // Créer le nom de fichier
        String fileName = beneficiare + "_" + dateAffectation + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + fileName);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
