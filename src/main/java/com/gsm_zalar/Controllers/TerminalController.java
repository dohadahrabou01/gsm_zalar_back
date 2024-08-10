package com.gsm_zalar.Controllers;

import com.gsm_zalar.DTO.TerminalDTO;
import com.gsm_zalar.Models.Terminal;
import com.gsm_zalar.Repositories.TerminalRepository;
import com.gsm_zalar.Services.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/terminals")
public class TerminalController {

    @Autowired
    private TerminalService terminalService;
@Autowired
private TerminalRepository terminalRepository;
    @PostMapping
    public ResponseEntity<TerminalDTO> createTerminal(@RequestBody TerminalDTO terminalDTO) {
        TerminalDTO createdTerminal = terminalService.createTerminal(terminalDTO);
        return new ResponseEntity<>(createdTerminal, HttpStatus.CREATED);
    }
    @GetMapping("/pourcentage-affectation")
    public double obtenirPourcentageAffectation() {
        return terminalService.calculerPourcentageAffectation();
    }
    @GetMapping("/by-code/{code}")
    public ResponseEntity<List<TerminalDTO>> getTerminalsByCode(@PathVariable String code) {
        List<TerminalDTO> terminalDTOList = terminalService.getTerminalByCode(code);
        return ResponseEntity.ok(terminalDTOList);
    }
    @GetMapping
    public ResponseEntity<List<TerminalDTO>> getAllTerminals() {
        List<TerminalDTO> terminals = terminalService.getAllTerminals();
        return new ResponseEntity<>(terminals, HttpStatus.OK);
    }
    @PostMapping("/import")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a valid file");
        }

        System.out.println("Received file: " + file.getOriginalFilename()); // Debug line

        try {
            List<Terminal> terminales = terminalService.importFromExcel(file);
            terminalRepository.saveAll(terminales);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/terminals/download/")
                    .path(file.getOriginalFilename())
                    .toUriString();

            return ResponseEntity.ok("File uploaded successfully: " + fileDownloadUri);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/Historique")
    public ResponseEntity<List<TerminalDTO>> getHistorique() {
        List<TerminalDTO> terminals = terminalService.getHistorique();
        return new ResponseEntity<>(terminals, HttpStatus.OK);
    }
    @GetMapping("/ByEmail")
    public ResponseEntity<List<TerminalDTO>> getByEmail(String email) {
        List<TerminalDTO> terminals = terminalService.getByEmail(email);
        return new ResponseEntity<>(terminals, HttpStatus.OK);
    }
    @GetMapping("/byfilliale")
    public ResponseEntity<List<TerminalDTO>> getByFilliales(@RequestParam String email) {
        List<TerminalDTO> terminaux = terminalService.getByFilliales(email);
        return new ResponseEntity<>(terminaux, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TerminalDTO> getTerminalById(@PathVariable("id") int id) {
        TerminalDTO terminal = terminalService.getTerminalById(id);
        if (terminal != null) {
            return new ResponseEntity<>(terminal, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TerminalDTO> updateTerminal(@PathVariable("id") int id, @RequestBody TerminalDTO terminalDTO) {
        TerminalDTO updatedTerminal = terminalService.updateTerminal(id, terminalDTO);
        if (updatedTerminal != null) {
            return new ResponseEntity<>(updatedTerminal, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerminal(@PathVariable("id") int id, @RequestParam String email) {
        terminalService.deleteTerminal(id, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/recuperer/{id}")
    public ResponseEntity<Void> recupererTerminal(@PathVariable("id") int id) {
        terminalService.recupererTerminal(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
