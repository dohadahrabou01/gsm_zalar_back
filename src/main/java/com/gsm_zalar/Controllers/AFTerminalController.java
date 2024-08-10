package com.gsm_zalar.Controllers;

import com.gsm_zalar.DTO.AFTerminalDTO;
import com.gsm_zalar.Models.AFTerminal;
import com.gsm_zalar.Repositories.AFTerminalRepository;
import com.gsm_zalar.Services.AFTerminalService;
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
@RequestMapping("/api/afterminals")
public class AFTerminalController {

    @Autowired
    private AFTerminalService afTerminalService;
    @Autowired
    private AFTerminalRepository afTerminalRepository;

    @PostMapping
    public ResponseEntity<AFTerminal> createAFTerminal(@RequestParam String imei,
                                                       @RequestParam int beneficiareId,
                                                       @RequestParam String affectantEmail) {
        try {
            AFTerminal newTerminal = afTerminalService.create(imei,beneficiareId,affectantEmail);
            return ResponseEntity.ok(newTerminal);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PutMapping("/update-duree-max")
    public ResponseEntity<String> updateDureeMax(@RequestParam int duree) {

        afTerminalService.Duree_MAX(duree);
        return ResponseEntity.ok("Duration updated successfully for all terminals.");

    }

    @GetMapping("/all")
    public ResponseEntity<List<AFTerminalDTO>> getAllAFNumeroDTOs() {
        List<AFTerminalDTO> afTerminalDTOs = afTerminalService.getAllAFTerminal();
        return ResponseEntity.ok(afTerminalDTOs);
    }
    @GetMapping("/Historique")
    public ResponseEntity<List<AFTerminalDTO>> getHistorique() {
        List<AFTerminalDTO> afTerminalDTOs = afTerminalService.getHistorique();
        return ResponseEntity.ok(afTerminalDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AFTerminalDTO> getById(@PathVariable("id") int id) {
       AFTerminalDTO afTerminalDTO = afTerminalService.getById(id);
        return ResponseEntity.ok(afTerminalDTO);
    }
    @GetMapping("/ByEmail")
    public ResponseEntity<List<AFTerminalDTO>> getByEmail(String email) {
        List<AFTerminalDTO> afTerminalDTOs  = afTerminalService.getByEmail(email);
        return ResponseEntity.ok(afTerminalDTOs);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> updateAFTerminal(
            @PathVariable("id") int id)
          {
        Boolean isUpdated = afTerminalService.updateAFTerminal(id);
        if (isUpdated) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }
    @PutMapping("/reject/{id}")
    public ResponseEntity<Boolean> rejectAFTerminal(
            @PathVariable("id") int id)
    {
        Boolean isUpdated = afTerminalService.rejectedAFTerminal(id);
        if (isUpdated) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }
    @GetMapping("/preuve/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable int id) {
        return  afTerminalService.getImage(id)
                .map(attachement -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachement.getImageName() + "\"")
                        .body(attachement.getImageData()))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/upload/{id}")
    public ResponseEntity<String> uploadFile(@PathVariable int id,@RequestParam("file") MultipartFile file) throws IOException {
        afTerminalService.storeFile(id,file);
        return ResponseEntity.ok("File uploaded successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAFTerminal(
            @PathVariable int id,
            @RequestParam String email) {

        try {
            afTerminalService.deleteAFTerminal(id, email);
            return ResponseEntity.ok().body("AFTerminal successfully deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PutMapping("/recuperer/{id}")
    public ResponseEntity<?> recupererAFTerminal(
            @PathVariable int id) {

        try {
            afTerminalService.recupererAFTerminal(id);
            return ResponseEntity.ok().body("AFTerminal successfully deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
