package com.gsm_zalar.Controllers;

import com.gsm_zalar.Models.AFTerminal;
import com.gsm_zalar.Repositories.AFTerminalRepository;
import com.gsm_zalar.Services.PdfService;
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
@RequestMapping("/api/pdf")
public class PdfController {

    private final PdfService pdfService;
    @Autowired
    AFTerminalRepository afTerminalRepository;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable("id") int id) {
        // Generate PDF
        ByteArrayInputStream bais = pdfService.generatePdf(id);

        // Fetch AFTerminal
        Optional<AFTerminal> afNumeroOptional = afTerminalRepository.findById(id);

        if (!afNumeroOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        AFTerminal afNumero = afNumeroOptional.get();
        String beneficiare = afNumero.getBeneficiare().getNom();
        String dateAffectation = afNumero.getDate_Affectation();

        // Format date for filename
        String formattedDate = dateAffectation.replace("/", "-"); // Example: replace slashes with dashes

        // Create filename
        String fileName = beneficiare + "_" + formattedDate + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(bais));
    }

}
