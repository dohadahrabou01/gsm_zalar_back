package com.gsm_zalar.Models;

import com.gsm_zalar.DTO.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ExcelExporter {

    public static void exportBeneficiareDTOToExcel(List<BeneficiareDTO> beneficiareDTOList, OutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Beneficiares");

        // Header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nom");
        headerRow.createCell(2).setCellValue("Prenom");
        headerRow.createCell(3).setCellValue("Code");
        headerRow.createCell(4).setCellValue("Grade");
        headerRow.createCell(5).setCellValue("Filliale Libelle");
        headerRow.createCell(6).setCellValue("Multiple");

        // Data rows
        int rowNum = 1;
        for (BeneficiareDTO dto : beneficiareDTOList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(dto.getId());
            row.createCell(1).setCellValue(dto.getNom());
            row.createCell(2).setCellValue(dto.getPrenom());
            row.createCell(3).setCellValue(dto.getCode());
            row.createCell(4).setCellValue(dto.getGrade());
            row.createCell(5).setCellValue(dto.getFillialeLibelle());
            row.createCell(6).setCellValue(dto.getMultiple());
        }

        // Write the output to the provided OutputStream
        workbook.write(outputStream);
        workbook.close();
    }
    public static void exportTerminalsToExcel(List<TerminalDTO> terminals, ByteArrayOutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Terminals");

        // Création de l'en-tête
        Row headerRow = sheet.createRow(0);
        String[] columnNames = {"ID", "Marque", "Model", "RAM", "ROM", "IMEI", "Date Acquisition", "Durée Garantie", "Affectation", "Fournisseur", "Filliale", "Grade"};

        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(createHeaderCellStyle(workbook));
        }

        // Remplir les données
        int rowNum = 1;
        for (TerminalDTO terminal : terminals) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(terminal.getId());
            row.createCell(1).setCellValue(terminal.getMarque());
            row.createCell(2).setCellValue(terminal.getModel());
            row.createCell(3).setCellValue(terminal.getRam());
            row.createCell(4).setCellValue(terminal.getRom());
            row.createCell(5).setCellValue(terminal.getImei());
            row.createCell(6).setCellValue(terminal.getDateAcquisition());
            row.createCell(7).setCellValue(terminal.getDureeGarantie());
            row.createCell(8).setCellValue(terminal.getAffectation());
            row.createCell(9).setCellValue(terminal.getFournisseur());
            row.createCell(10).setCellValue(terminal.getFillialeLibelle());
            row.createCell(11).setCellValue(terminal.getGrade());
        }

        // Écriture du fichier Excel
        workbook.write(outputStream);
        workbook.close();
    }
    public static void exportNumerosToExcel(List<NumeroDTO> numeros, ByteArrayOutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Numeros");

        // Création de l'en-tête
        Row headerRow = sheet.createRow(0);
        String[] columnNames = {"ID", "Numero", "Serie", "Pin", "Puk", "Affectation", "Operateur", "Actif", "Forfait", "Filliale"};

        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(createHeaderCellStyle(workbook));
        }

        // Remplir les données
        int rowNum = 1;
        for (NumeroDTO numero : numeros) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(numero .getId());
            row.createCell(1).setCellValue(numero .getNumero());
            row.createCell(2).setCellValue(numero.getSerie());
            row.createCell(3).setCellValue(numero.getPin());
            row.createCell(4).setCellValue(numero.getPuk());
            row.createCell(5).setCellValue(numero.getAffectation());
            row.createCell(6).setCellValue(numero.getOperateur());
            row.createCell(7).setCellValue(numero.getActif());
            row.createCell(8).setCellValue(numero.getForfait());
            row.createCell(9).setCellValue(numero.getFillialeLibelle());

        }

        // Écriture du fichier Excel
        workbook.write(outputStream);
        workbook.close();
    }

    public static void exportAFTerminalsToExcel(List<AFTerminalDTO> afTerminalDTOSs, ByteArrayOutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Affectation_Terminaux");

        // Création de l'en-tête
        Row headerRow = sheet.createRow(0);
        String[] columnNames = {"ID", "Nom", "Prenom", "Filliale", "Marque ", "Imei", "Date D'affectation", "Validation"};

        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(createHeaderCellStyle(workbook));
        }

        // Remplir les données
        int rowNum = 1;
        for (AFTerminalDTO afTerminalDTO : afTerminalDTOSs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(afTerminalDTO .getId());
            row.createCell(1).setCellValue(afTerminalDTO.getBeneficiareDTO().getNom()+" "+afTerminalDTO.getBeneficiareDTO().getPrenom());
            row.createCell(2).setCellValue(afTerminalDTO.getBeneficiareDTO().getGrade());
            row.createCell(3).setCellValue(afTerminalDTO.getBeneficiareDTO().getFillialeLibelle());
            row.createCell(4).setCellValue(afTerminalDTO.getTerminalDTO().getMarque());
            row.createCell(5).setCellValue(afTerminalDTO.getTerminalDTO().getImei());
            row.createCell(6).setCellValue(afTerminalDTO.getDate_affectation());
            row.createCell(7).setCellValue(afTerminalDTO.getValidation());


        }

        // Écriture du fichier Excel
        workbook.write(outputStream);
        workbook.close();
    }
    public static void exportAFNumerosToExcel(List<AFNumeroDTO> afNumeroDTOSs, ByteArrayOutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Affectation_Terminaux");

        // Création de l'en-tête
        Row headerRow = sheet.createRow(0);
        String[] columnNames = {"ID", "Nom ", "Prenom", "Filliale", "Numero ", "Forfait", "Date D'affectation"};

        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(createHeaderCellStyle(workbook));
        }

        // Remplir les données
        int rowNum = 1;
        for (AFNumeroDTO afNumeroDTO : afNumeroDTOSs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(afNumeroDTO.getId());
            row.createCell(1).setCellValue(afNumeroDTO.getBeneficiareDTO().getNom());
            row.createCell(2).setCellValue(afNumeroDTO.getBeneficiareDTO().getPrenom());
            row.createCell(3).setCellValue(afNumeroDTO.getBeneficiareDTO().getFillialeLibelle());
            row.createCell(4).setCellValue(afNumeroDTO.getNumeroDTO().getNumero());
            row.createCell(5).setCellValue(afNumeroDTO.getNumeroDTO().getForfait());
            row.createCell(6).setCellValue(afNumeroDTO.getDate_affectation());



        }

        // Écriture du fichier Excel
        workbook.write(outputStream);
        workbook.close();
    }

    private static CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}
