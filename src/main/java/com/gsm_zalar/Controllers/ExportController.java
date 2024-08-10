package com.gsm_zalar.Controllers;

import com.gsm_zalar.DTO.*;
import com.gsm_zalar.Models.ExcelExporter;
import com.gsm_zalar.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    @Autowired
    private BeneficiareService beneficiareService;
    @Autowired
    private TerminalService terminalService;// Assurez-vous d'injecter cette dépendance
    @Autowired
    private NumeroService numeroService;
    @Autowired
    private AFTerminalService afTerminalService;
    @Autowired
    private AFNumeroService afNumeroService;

    @GetMapping("/beneficiares")
    public ResponseEntity<byte[]> exportBeneficiaresToExcel() {
        try {
            // Créer un flux de sortie en mémoire pour le fichier Excel
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Obtenez les données à exporter
            List<BeneficiareDTO> beneficiareDTOList = beneficiareService.getAllBeneficiares();

            // Exporter les données au format Excel
            ExcelExporter.exportBeneficiareDTOToExcel(beneficiareDTOList, outputStream);

            // Configurer les en-têtes de réponse pour télécharger le fichier
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=beneficiares.xlsx");
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            // Convertir le flux en tableau de bytes pour la réponse
            byte[] excelData = outputStream.toByteArray();

            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/terminals")
    public ResponseEntity<byte[]> exportTerminalsToExcel() {
        try {
            // Créer un flux de sortie en mémoire pour le fichier Excel
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Obtenez les données à exporter
            List<TerminalDTO> terminalDTOList = terminalService.getAllTerminals();

            // Exporter les données au format Excel
            ExcelExporter.exportTerminalsToExcel(terminalDTOList, outputStream);

            // Configurer les en-têtes de réponse pour télécharger le fichier
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=terminals.xlsx");
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            // Convertir le flux en tableau de bytes pour la réponse
            byte[] excelData = outputStream.toByteArray();

            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/numeros")
    public ResponseEntity<byte[]> exportNumerosToExcel() {
        try {
            // Créer un flux de sortie en mémoire pour le fichier Excel
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Obtenez les données à exporter
            List<NumeroDTO> numeroDTOList = numeroService.getAllNumeros();

            // Exporter les données au format Excel
            ExcelExporter.exportNumerosToExcel(numeroDTOList, outputStream);

            // Configurer les en-têtes de réponse pour télécharger le fichier
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=numeros.xlsx");
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            // Convertir le flux en tableau de bytes pour la réponse
            byte[] excelData = outputStream.toByteArray();

            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/afterminaux")
    public ResponseEntity<byte[]> exportAffectationTerminalToExcel() {
        try {
            // Créer un flux de sortie en mémoire pour le fichier Excel
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Obtenez les données à exporter
            List<AFTerminalDTO> afTerminalDTOList = afTerminalService.getAllAFTerminal();

            // Exporter les données au format Excel
            ExcelExporter.exportAFTerminalsToExcel(afTerminalDTOList, outputStream);

            // Configurer les en-têtes de réponse pour télécharger le fichier
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=affectation_terminaux.xlsx");
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            // Convertir le flux en tableau de bytes pour la réponse
            byte[] excelData = outputStream.toByteArray();

            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/afNumeros")
    public ResponseEntity<byte[]> exportAffectationNumeroToExcel() {
        try {
            // Créer un flux de sortie en mémoire pour le fichier Excel
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Obtenez les données à exporter
            List<AFNumeroDTO> afNumeroDTOList = afNumeroService.getAllAFNumeroDTOs();

            // Exporter les données au format Excel
            ExcelExporter.exportAFNumerosToExcel( afNumeroDTOList, outputStream);

            // Configurer les en-têtes de réponse pour télécharger le fichier
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=affectation_numeros.xlsx");
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            // Convertir le flux en tableau de bytes pour la réponse
            byte[] excelData = outputStream.toByteArray();

            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
