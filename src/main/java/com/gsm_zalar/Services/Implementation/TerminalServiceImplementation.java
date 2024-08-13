package com.gsm_zalar.Services.Implementation;

import com.gsm_zalar.DTO.TerminalDTO;
import com.gsm_zalar.Models.*;
import com.gsm_zalar.Repositories.*;
import com.gsm_zalar.Services.TerminalService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TerminalServiceImplementation implements TerminalService {

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private FillialeRepository fillialeRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FournisseurRepository fournisseurRepository;
    @Autowired
    private BeneficiaireRepository beneficiareRepository;
    @Autowired
    private AFTerminalRepository afTerminalRepository;
    private static final long EXCEL_BASE_DATE = 25569;
    @Override
    public List<TerminalDTO> getByFilliales(String email){
        User user = (User) userRepository.findByEmail(email);


        return terminalRepository.findAll().stream()
                .filter(terminal -> user.equals(terminal.getFillialeterminale().getResponsable()) || user.equals(terminal.getFillialeterminale().getGerant()))
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public TerminalDTO createTerminal(TerminalDTO terminalDTO) {
        Terminal terminal = new Terminal();
        // Map TerminalDTO fields to Terminal entity

        terminal.setMarque(terminalDTO.getMarque());
        terminal.setModel(terminalDTO.getModel());
        terminal.setRam(terminalDTO.getRam());
        terminal.setRom(terminalDTO.getRom());
        terminal.setImei(terminalDTO.getImei());
        terminal.setDateAcquisition(String.valueOf(LocalDate.now()));
        terminal.setDureeGarantie(terminalDTO.getDureeGarantie());
        terminal.setGrade(Grade.valueOf(terminalDTO.getGrade()));

        terminal.setFournisseur(fournisseurRepository.findByLibelle(terminalDTO.getFournisseur()));
        terminal.setFillialeterminal(fillialeRepository.findByLibelle(terminalDTO.getFillialeLibelle()));


        Terminal savedTerminal = terminalRepository.save(terminal);
        return entityToDTO(savedTerminal);
    }

    @Override
    public List<TerminalDTO> getAllTerminals() {
        return terminalRepository.findAll().stream()
                .filter(terminal -> terminal.getIsDeleted() == false)
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<TerminalDTO> getHistorique() {
        return terminalRepository.findAll().stream()
                .filter(terminal -> terminal.getIsDeleted() ==true)
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TerminalDTO getTerminalById(int id) {
        Optional<Terminal> terminal = terminalRepository.findById(id);
        return terminal.map(this::entityToDTO).orElse(null);
    }


    @Override
    public TerminalDTO updateTerminal(int id, TerminalDTO terminalDTO) {
        Optional<Terminal> optionalTerminal = terminalRepository.findById(id);
        if (optionalTerminal.isPresent()) {
            Terminal terminal = optionalTerminal.get();
            // Update Terminal entity fields based on TerminalDTO
            terminal.setMarque(terminalDTO.getMarque());
            terminal.setModel(terminalDTO.getModel());
            terminal.setRam(terminalDTO.getRam());
            terminal.setRom(terminalDTO.getRom());
            terminal.setImei(terminalDTO.getImei());
            terminal.setDateAcquisition(terminalDTO.getDateAcquisition());
            terminal.setDureeGarantie(terminalDTO.getDureeGarantie());
            terminal.setGrade(Grade.valueOf(terminalDTO.getGrade()));

            // Fetch related entities
            Optional<Fournisseur> fournisseur = Optional.ofNullable(fournisseurRepository.findByLibelle(terminalDTO.getFournisseur()));
            if (fournisseur != null) {
                terminal.setFournisseur(fournisseur.get());
            } else {
                throw new IllegalArgumentException("Fournisseur not found");
            }

            Optional<Filliale> filliale = Optional.ofNullable(fillialeRepository.findByLibelle(terminalDTO.getFillialeLibelle()));
            if (filliale != null) {
                terminal.setFillialeterminal(filliale.get());
            } else {
                throw new IllegalArgumentException("Filliale not found");
            }

            terminal = terminalRepository.save(terminal);
            return entityToDTO(terminal);
        }
        throw new IllegalArgumentException("Terminal not found");
    }


    @Override
    public double calculerPourcentageAffectation() {
        long totalNumeros = terminalRepository.countByDeletedFalse();
        long numerosAffectes = terminalRepository.countByAffectationTrueAndDeletedFalse();

        if (totalNumeros == 0) {
            return 0.0;
        }

        return (double) numerosAffectes / totalNumeros * 100;
    }



    @Override
    public void deleteTerminal(int id,String email) {
        Optional<Terminal> numeroOptional = terminalRepository.findById(id);
        if (numeroOptional.isPresent()) {
            Terminal  numero = numeroOptional.get();
            numero.setDeleted(true);
            numero.setDeletedBy(email);
            numero.setDateDeletion(String.valueOf(LocalDate.now()));
            terminalRepository.save(numero);
        }
    }
    @Override
    public void recupererTerminal(int id) {
        Optional<Terminal> numeroOptional = terminalRepository.findById(id);
        if (numeroOptional.isPresent()) {
            Terminal  numero = numeroOptional.get();
            numero.setDeleted(false);

            terminalRepository.save(numero);
        }
    }
    @Override
    public List<TerminalDTO> getTerminalByCode(String code){
        Beneficiare beneficiare=beneficiareRepository.findByCode(code);

        List<AFTerminal> afterminals=afTerminalRepository.findByBeneficiare(beneficiare);
        List<Terminal> terminals=new ArrayList<>();
        for(AFTerminal afTerminal:afterminals){
            if(afTerminal.getValidation()==Validation.VALIDEE){
            terminals.add(afTerminal.getTerminal());}
        }
        List<TerminalDTO> terminalDTOList=new ArrayList<>();
        for(Terminal terminal :terminals){
            TerminalDTO terminalDTO=new TerminalDTO();
            terminalDTO.setMarque(terminal.getMarque() != null ? terminal.getMarque() : "None");
            terminalDTO.setModel(terminal.getModel() != null ? terminal.getModel() : "None");
            terminalDTO.setRam(terminal.getRam() != null ? terminal.getRam() : "None");
            terminalDTO.setRom(terminal.getRom() != null ? terminal.getRom() : "None");
            terminalDTO.setImei(terminal.getImei() != null ? terminal.getImei() : "None");
            terminalDTO.setDateAcquisition(terminal.getDateAcquisition() != null ? terminal.getDateAcquisition() : "None");
            terminalDTO.setDureeGarantie(terminal.getDureeGarantie()!=-1?terminal.getDureeGarantie():-1 );
            terminalDTO.setAffectation( terminal.getAffectation() );
            terminalDTO.setFournisseur(terminal.getFournisseur() != null ? terminal.getFournisseur().getLibelle() : "None");
            terminalDTO.setFillialeLibelle(terminal.getFilliale() != null ? terminal.getFilliale().getLibelle() : "None");
            terminalDTO.setGrade(terminal.getGrade() != null ? terminal.getGrade().name() : "None");
            terminalDTOList.add(terminalDTO);
        }
        return terminalDTOList;
    }

    public TerminalDTO entityToDTO(Terminal terminal) {
        TerminalDTO dto = new TerminalDTO();
        dto.setId(terminal.getId());
        dto.setMarque(terminal.getMarque() != null ? terminal.getMarque() : "None");
        dto.setModel(terminal.getModel() != null ? terminal.getModel() : "None");
        dto.setRam(terminal.getRam() != null ? terminal.getRam() : "None");
        dto.setRom(terminal.getRom() != null ? terminal.getRom() : "None");
        dto.setImei(terminal.getImei() != null ? terminal.getImei() : "None");
        dto.setDateAcquisition(terminal.getDateAcquisition() != null ? terminal.getDateAcquisition() : "None");
        dto.setDureeGarantie( terminal.getDureeGarantie() );
        dto.setAffectation( terminal.getAffectation() );
        dto.setFournisseur(terminal.getFournisseur() != null ? terminal.getFournisseur().getLibelle() : "None");
        dto.setFillialeLibelle(terminal.getFilliale() != null ? terminal.getFilliale().getLibelle() : "None");
        dto.setGrade(terminal.getGrade() != null ? terminal.getGrade().name() : "None");
        // Ajoutez tous les autres champs de votre DTO ici

        return dto;
    }

    @Override

    public List<Terminal> importFromExcel(MultipartFile file) {
        List<Terminal> terminales = new ArrayList<>();
        List<Terminal> existingTerminals = terminalRepository.findAll();

        int maxId = existingTerminals.stream()
                .mapToInt(Terminal::getId)
                .max()
                .orElse(0);

        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withDelimiter(';') // Délimiteur pour CSV
                    .withFirstRecordAsHeader());

            // Définir les en-têtes attendus
            Set<String> expectedHeaders = new HashSet<>();
            expectedHeaders.add("Marque");
            expectedHeaders.add("Model");
            expectedHeaders.add("Ram");
            expectedHeaders.add("Rom");
            expectedHeaders.add("IMEI");
            expectedHeaders.add("Date Acquisition");
            expectedHeaders.add("Garantie");
            expectedHeaders.add("Fournisseur");
            expectedHeaders.add("Filliale");
            expectedHeaders.add("Grade");

            // En-têtes trouvés
            Set<String> foundHeaders = new HashSet<>(csvParser.getHeaderNames());

            // Vérifier si tous les en-têtes attendus sont présents
            if (!foundHeaders.containsAll(expectedHeaders)) {
                // Afficher les en-têtes manquants
                for (String header : expectedHeaders) {
                    if (!foundHeaders.contains(header)) {
                      System.out.println("En-tête manquant : " + header);
                    }
                }
                return null;
            }

            for (CSVRecord csvRecord : csvParser) {
                maxId++;

                Terminal terminal = new Terminal();
                terminal.setMarque(csvRecord.get("Marque").trim());
                terminal.setModel(csvRecord.get("Model").trim());
                terminal.setRam(csvRecord.get("Ram").trim());
                terminal.setRom(csvRecord.get("Rom").trim());

                String imei = csvRecord.get("IMEI").trim();
                terminal.setImei(imei != null && !imei.isEmpty() ? imei : "Vide" + (maxId + 1));
                terminal.setDateAcquisition(convertDateFormat(csvRecord.get("Date Acquisition").trim()));

                Integer dureeGarantie = parseInteger(csvRecord.get("Garantie").trim());
                terminal.setDureeGarantie(dureeGarantie != null ? dureeGarantie : 0); // Valeur par défaut si null

                terminal.setAffectation(false); // Valeur par défaut si null

                terminal.setFournisseur(fournisseurRepository.findByLibelle(csvRecord.get("Fournisseur").trim()));
                terminal.setFillialeterminal(fillialeRepository.findByLibelle(csvRecord.get("Filliale").trim()) != null
                        ? fillialeRepository.findByLibelle(csvRecord.get("Filliale").trim())
                        : null);
                terminal.setGrade(Grade.valueOf(csvRecord.get("Grade").trim().toUpperCase())); // Convertir en majuscule pour correspondre à l'énumération

                if (!isTerminalExist(terminal)) {
                    terminales.add(terminal);
                } else {
                    System.out.println("Terminal déjà existant: " + terminal);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'importation: " + e.getMessage());
        }

        return terminales;
    }


    public boolean isTerminalExist(Terminal terminal) {
        // Utiliser la méthode du repository pour vérifier si le terminal existe déjà
        return terminalRepository.existsByImeiAndMarqueAndModel(
                terminal.getImei(), terminal.getMarque(), terminal.getModel()
        );
    }

    // Méthodes auxiliaires pour parsing
    private Integer parseInteger(String value) {
        try {
            return value != null && !value.isEmpty() ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Boolean parseBoolean(String value) {
        return value != null ? Boolean.parseBoolean(value) : null;
    }


    private String getCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private String convertDateFormat(String dateStr) {
        // Définir les formats d'entrée et de sortie
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Convertir la chaîne de caractères en objet Date
            Date date = inputFormat.parse(dateStr);

            // Convertir l'objet Date en chaîne de caractères avec le nouveau format
            return outputFormat.format(date);
        } catch (ParseException e) {
            // Gérer les exceptions liées au format de la date
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public List<TerminalDTO> getByEmail(String email) {
        List<Terminal> terminals = terminalRepository.findAll();
        List<Terminal> terminalList = new ArrayList<>();
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        List<Filliale> fillialeList = fillialeRepository.findAll();
        List<Filliale> filliales = new ArrayList<>();
        Filliale filliale1 = null;

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String roleName = user.getRole().name();

            if (roleName.equals("RSI")) {
                for (Filliale filliale : fillialeList) {
                    if (filliale.getResponsable() != null && filliale.getResponsable().equals(user)) {
                        filliales.add(filliale);
                    }
                }
                for (Terminal terminal :terminals) {
                    if (!terminal.getDeleted() && terminal.getFilliale()!=null && filliales.contains(terminal.getFilliale())) {
                        terminalList.add(terminal);
                    }
                }
            } else if (roleName.equals("SI")) {
                for (Filliale filliale : fillialeList) {
                    if (filliale.getGerant() != null && filliale.getGerant().equals(user)) {
                        filliale1 = filliale;
                        break;
                    }
                }
                for (Terminal terminal :terminals) {
                    if (!terminal.getDeleted() && terminal.getFilliale()!=null && terminal.getFilliale().equals(filliale1)) {
                        terminalList.add(terminal);
                    }
                }
            }
        }
        return terminalList.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());

    }
}
