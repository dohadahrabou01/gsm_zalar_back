package com.gsm_zalar.Controllers;

import com.gsm_zalar.DTO.ForfaitDTO;
import com.gsm_zalar.Services.ForfaitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forfaits")
public class ForfaitController {

    @Autowired
    private ForfaitService forfaitService;

    @PostMapping
    public ResponseEntity<ForfaitDTO> createForfait(@RequestBody ForfaitDTO forfaitDTO) {
        ForfaitDTO createdForfait = forfaitService.createForfait(forfaitDTO);
        return ResponseEntity.ok(createdForfait);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ForfaitDTO> getForfaitById(@PathVariable int id) {
        ForfaitDTO forfaitDTO = forfaitService.getForfaitById(id);
        return ResponseEntity.ok(forfaitDTO);
    }

    @GetMapping("forfaits")
    public ResponseEntity<List<ForfaitDTO>> getAllForfaits() {
        List<ForfaitDTO> forfaits = forfaitService.getAllForfaits();
        return ResponseEntity.ok(forfaits);
    }
    @GetMapping("/Historique")
    public ResponseEntity<List<ForfaitDTO>> getHistorique() {
        List<ForfaitDTO> forfaits = forfaitService.getHistorique();
        return ResponseEntity.ok(forfaits);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ForfaitDTO> updateForfait(@PathVariable int id, @RequestBody ForfaitDTO forfaitDTO) {
        ForfaitDTO updatedForfait = forfaitService.updateForfait(id, forfaitDTO);
        return ResponseEntity.ok(updatedForfait);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForfait(@PathVariable int id) {
        forfaitService.deleteForfait(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/recuperer/{id}")
    public ResponseEntity<Void> recupererForfait(@PathVariable int id) {
        forfaitService.recupererForfait(id);
        return ResponseEntity.noContent().build();
    }
}
