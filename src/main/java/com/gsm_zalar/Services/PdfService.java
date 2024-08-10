package com.gsm_zalar.Services;

import com.gsm_zalar.DTO.AFTerminalDTO;
import com.gsm_zalar.Models.AFTerminal;
import com.gsm_zalar.Repositories.AFTerminalRepository;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

@Service
public class PdfService {
    @Autowired
    private AFTerminalRepository afTerminalRepository;

    public ByteArrayInputStream generatePdf(int id) {
        Optional<AFTerminal> terminal=afTerminalRepository.findById(id);
        AFTerminal afTerminal=terminal.get();
        afTerminal.setImprim(true);
        afTerminalRepository.save(afTerminal);
        AFTerminalDTO afTerminalDTO=new AFTerminalDTO();
        afTerminalDTO.setId(afTerminal.getId());
        afTerminalDTO.setBeneficiareDTO(afTerminal.getBeneficiare());
        afTerminalDTO.setTerminalDTO(afTerminal.getTerminal());
        afTerminalDTO.setDate_Affectation(afTerminal.getDate_Affectation());



        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Load image from classpath
            String imagePath = "static/LOGO_ZALAR.png";
            ImageData imageData = ImageDataFactory.create(getClass().getClassLoader().getResource(imagePath).toURI().toString());
            Image logo = new Image(imageData);

            // Resize image
            logo.scaleToFit(100, 50); // Adjust width and height as needed
            logo.setHorizontalAlignment(HorizontalAlignment.LEFT);

            // Create table and add image
            float[] columnWidths = {1, 4, 1}; // Adjust column widths as needed
            Table table = new Table(columnWidths);

            // Adjust header height
            float headerHeight = 50f;

            // Add the image cell
            Cell logoCell = new Cell().add(logo)
                    .setBorder(new SolidBorder(0.5f))
                    .setTextAlignment(TextAlignment.LEFT)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE) // Center vertically
                    .setPaddingLeft(10) // Margin left (adjust as needed)
                    .setPaddingRight(10) // Margin right (adjust as needed)
                    .setHeight(headerHeight);
            table.addCell(logoCell);

            // Create a bold font
            PdfFont boldFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);

            // Add the title cell with bold text
            Paragraph title = new Paragraph("FICHE D'AFFECTATION DU MATERIEL")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12)
                    .setPaddingLeft(17) // Margin left (adjust as needed)
                    .setPaddingRight(10)
                    .setFont(boldFont);
            Cell titleCell = new Cell().add(title)
                    .setBorder(new SolidBorder(0.5f))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE) // Center vertically
                    .setHeight(headerHeight);
            table.addCell(titleCell);

            // Add the right text cell
            Paragraph rightText = new Paragraph("CODE : _______________\nDATE DE MISE A JOUR : _______________")
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(7);
            Cell rightTextCell = new Cell().add(rightText)
                    .setBorder(new SolidBorder(0.5f))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE) // Center vertically
                    .setHeight(headerHeight);
            table.addCell(rightTextCell);

            document.add(table);

            // Add subtitle
            document.add(new Paragraph("1 - REFERENCES DU MATERIEL")
                    .setFont(boldFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginTop(20));

            // Create table for references
            float[] referenceColumnWidths = {180,350}; // Adjust column widths as needed
            Table referenceTable = new Table(referenceColumnWidths);
            float cellFontSize = 10f;

            // Add rows with vertical titles
            referenceTable.addCell(new Cell().add(new Paragraph("Désignation")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));
            referenceTable.addCell(new Cell().add(new Paragraph(afTerminalDTO.getBeneficiareDTO().getFillialeLibelle())).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));

            referenceTable.addCell(new Cell().add(new Paragraph("Marque")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));
            referenceTable.addCell(new Cell().add(new Paragraph(afTerminalDTO.getTerminalDTO().getMarque())).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));

            referenceTable.addCell(new Cell().add(new Paragraph("Type")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));
            referenceTable.addCell(new Cell().add(new Paragraph(afTerminalDTO.getTerminalDTO().getRam()+"/"+afTerminalDTO.getTerminalDTO().getRom())).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));

            referenceTable.addCell(new Cell().add(new Paragraph("Identification ou référence")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));
            referenceTable.addCell(new Cell().add(new Paragraph(afTerminalDTO.getTerminalDTO().getImei())).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));

            referenceTable.addCell(new Cell().add(new Paragraph("Date d'acquisition")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));
            referenceTable.addCell(new Cell().add(new Paragraph(afTerminalDTO.getDate_affectation())).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));

            document.add(referenceTable);

            document.add(new Paragraph("2- ATTRIBUTAIRE DU MATERIEL")
                    .setFont(boldFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginTop(20));

            Table referenceTable1 = new Table(referenceColumnWidths);

            // Add rows with vertical titles
            referenceTable1.addCell(new Cell().add(new Paragraph("Nom & prénom")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));
            referenceTable1.addCell(new Cell().add(new Paragraph(afTerminalDTO.getBeneficiareDTO().getNom()+" "+afTerminalDTO.getBeneficiareDTO().getPrenom())).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));

            referenceTable1.addCell(new Cell().add(new Paragraph("Poste / Service")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));
            referenceTable1.addCell(new Cell().add(new Paragraph("")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));

            referenceTable1.addCell(new Cell().add(new Paragraph("Lieu")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));
            referenceTable1.addCell(new Cell().add(new Paragraph(afTerminalDTO.getBeneficiareDTO().getFillialeLibelle())).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));

            referenceTable1.addCell(new Cell().add(new Paragraph("Date d'affectation")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));
            referenceTable1.addCell(new Cell().add(new Paragraph(afTerminalDTO.getDate_affectation())).setFontSize(cellFontSize).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));

            referenceTable1.addCell(new Cell().add(new Paragraph("Date de reprise, retour ou réforme")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));
            referenceTable1.addCell(new Cell().add(new Paragraph("")).setFontSize(cellFontSize).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)));

            document.add(referenceTable1);
            document.add(new Paragraph("3- VISA DE MISE A DISPOSITION")
                    .setFont(boldFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginTop(20));
            float[] columnWidths1 = {100, 250,270}; // Ajustez les largeurs des colonnes en points
            Table table1 = new Table(columnWidths1);
            float minHeight = 100f;
            // Première colonne
            table1.addCell(new Cell().add(new Paragraph("")).setBorder(new SolidBorder(0.5f)));
            table1.addCell(new Cell().add(new Paragraph("Attributaire")).setBorder(new SolidBorder(0.5f)).setTextAlignment(TextAlignment.CENTER));
            table1.addCell(new Cell().add(new Paragraph("Direction SI")).setBorder(new SolidBorder(0.5f)).setTextAlignment(TextAlignment.CENTER));

            // Deuxième colonne
            table1.addCell(new Cell().add(new Paragraph("Date")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)).setTextAlignment(TextAlignment.CENTER));
            table1.addCell(new Cell().add(new Paragraph(afTerminalDTO.getDate_affectation())).setBorder(new SolidBorder(0.5f)).setFontSize(cellFontSize).setTextAlignment(TextAlignment.CENTER));
            table1.addCell(new Cell().add(new Paragraph("")).setBorder(new SolidBorder(0.5f)).setTextAlignment(TextAlignment.CENTER));

            // Troisième colonne
            table1.addCell(new Cell().add(new Paragraph("Nom & Prénom Signature")).setFontSize(cellFontSize).setMinHeight(minHeight).setBorder(new SolidBorder(0.5f)).setTextAlignment(TextAlignment.CENTER));
            table1.addCell(new Cell().add(new Paragraph("")).setBorder(new SolidBorder(0.5f)).setMinHeight(minHeight).setTextAlignment(TextAlignment.CENTER));
            table1.addCell(new Cell().add(new Paragraph("")).setBorder(new SolidBorder(0.5f)).setMinHeight(minHeight).setTextAlignment(TextAlignment.CENTER));

            // Fusionne les deux dernières cellules de la troisième colonne

            document.add(table1);
            document.add(new Paragraph("4- VISA DE RETRAIT, RETOUR OU REFORME")
                    .setFont(boldFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginTop(20));
             // Ajustez les largeurs des colonnes en points
            Table table2 = new Table(columnWidths1);

            // Première colonne
            table2.addCell(new Cell().add(new Paragraph("")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)).setTextAlignment(TextAlignment.CENTER));
            table2.addCell(new Cell().add(new Paragraph("Attributaire")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)).setTextAlignment(TextAlignment.CENTER));
            table2.addCell(new Cell().add(new Paragraph("Direction SI")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)).setTextAlignment(TextAlignment.CENTER));

            // Deuxième colonne
            table2.addCell(new Cell().add(new Paragraph("Date")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)).setTextAlignment(TextAlignment.CENTER));
            table2.addCell(new Cell().add(new Paragraph("")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)).setTextAlignment(TextAlignment.CENTER));
            table2.addCell(new Cell().add(new Paragraph("")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)).setTextAlignment(TextAlignment.CENTER));

            // Troisième colonne
            table2.addCell(new Cell().add(new Paragraph("Nom & Prénom Signature")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)).setMinHeight(minHeight).setTextAlignment(TextAlignment.CENTER));
            table2.addCell(new Cell().add(new Paragraph("")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)).setMinHeight(minHeight).setTextAlignment(TextAlignment.CENTER));
            table2.addCell(new Cell().add(new Paragraph("")).setFontSize(cellFontSize).setBorder(new SolidBorder(0.5f)).setMinHeight(minHeight).setTextAlignment(TextAlignment.CENTER));

            // Fusionne les deux dernières cellules de la troisième colonne

            document.add(table2);
            addFooter(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
    private void addFooter(Document document) {
        // Create footer paragraph
        Paragraph footer = new Paragraph("NB : En cas de retour du matériel (réaffectation, panne, réforme, etc.), la fiche d’affectation de matériel est renseignée à nouveau dans la partie correspondante.")
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20);

        // Add footer paragraph to the document
        document.add(footer);
    }

}
