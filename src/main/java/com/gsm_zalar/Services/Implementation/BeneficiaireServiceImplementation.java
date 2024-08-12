package com.gsm_zalar.Services.Implementation;


import com.gsm_zalar.DTO.BeneficiareDTO;
import com.gsm_zalar.Models.*;
import com.gsm_zalar.Repositories.*;
import com.gsm_zalar.Services.BeneficiareService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BeneficiaireServiceImplementation implements BeneficiareService  {
    @Autowired
    private BeneficiaireRepository beneficiareRepository;
    @Autowired
    private FillialeRepository fillialeRepository;
    @Autowired
    private NumeroRepository numeroRepository;
    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public BeneficiareDTO createBeneficiare(BeneficiareDTO beneficiareDTO) {
        // Vérifier si un bénéficiaire avec le même nom et prénom existe déjà
        Optional<Beneficiare> existingBeneficiare = beneficiareRepository.findByNomAndPrenom(beneficiareDTO.getNom(), beneficiareDTO.getPrenom());
        if (existingBeneficiare.isPresent()) {
            throw new IllegalArgumentException("Un bénéficiaire avec ce nom et prénom existe déjà.");
        }

        Beneficiare beneficiare = new Beneficiare();
        beneficiare.setNom(beneficiareDTO.getNom());
        beneficiare.setMultiple(beneficiareDTO.getMultiple());
        beneficiare.setPrenom(beneficiareDTO.getPrenom());
        beneficiare.setCode(generateUniqueCode());
        beneficiare.setGrade(Grade.valueOf(beneficiareDTO.getGrade()));
        beneficiare.setFilliale(fillialeRepository.findByLibelle(beneficiareDTO.getFillialeLibelle()));

        Beneficiare savedBeneficiare = beneficiareRepository.save(beneficiare);
        return entityToDTO(savedBeneficiare);
    }

    private String generateUniqueCode() {
        // Génère un code unique en utilisant UUID
        return UUID.randomUUID().toString();
    }
    @Override
    public List<BeneficiareDTO> getByEmail(String email) {
        List<Beneficiare> beneficiares = beneficiareRepository.findAll();
        List<Beneficiare> beneficiareList = new ArrayList<>();
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
                for (Beneficiare beneficiare : beneficiares) {
                    if (!beneficiare.getDeleted() && filliales.contains(beneficiare.getFilliale())) {
                        beneficiareList.add(beneficiare);
                    }
                }
            } else if (roleName.equals("SI")) {
                for (Filliale filliale : fillialeList) {
                    if (filliale.getGerant() != null && filliale.getGerant().equals(user)) {
                        filliale1 = filliale;
                        break;
                    }
                }
                for (Beneficiare beneficiare : beneficiares) {
                    if (!beneficiare.getDeleted() && beneficiare.getFilliale().equals(filliale1)) {
                        beneficiareList.add(beneficiare);
                    }
                }
            }
        }

        return beneficiareList.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }


@Override
public List<Beneficiare> importFromExcel(MultipartFile file) {
    List<Beneficiare> beneficiaires = new ArrayList<>();

    try (Reader reader = new InputStreamReader(file.getInputStream())) {
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withDelimiter(';') // Délimiteur pour CSV
                .withFirstRecordAsHeader());

        // Définir les en-têtes attendus
        Set<String> expectedHeaders = new HashSet<>();
        expectedHeaders.add("Nom");
        expectedHeaders.add("Prenom");
        expectedHeaders.add("Grade");
        expectedHeaders.add("Filliale");
        expectedHeaders.add("Multiple");

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
            String nom = csvRecord.get("Nom").trim();
            String prenom = csvRecord.get("Prenom").trim();

            Optional<Beneficiare> existingBeneficiaire = beneficiareRepository.findByNomAndPrenom(nom, prenom);
            if (existingBeneficiaire.isEmpty()) {
                Beneficiare beneficiaire = new Beneficiare();
                beneficiaire.setNom(nom);
                beneficiaire.setPrenom(prenom);
                beneficiaire.setGrade(Grade.valueOf(csvRecord.get("Grade").trim().toUpperCase())); // Convertir en majuscule pour correspondre à l'énumération

                beneficiaire.setCode(generateUniqueCode());

                beneficiaire.setFilliale(fillialeRepository.findByLibelle(csvRecord.get("Filliale").trim()) != null
                        ? fillialeRepository.findByLibelle(csvRecord.get("Filliale").trim())
                        : null);

                String multiple = csvRecord.get("Multiple").trim();
                boolean isMultiple = "OUI".equals(multiple); // Comparaison sensible à la casse

                beneficiaire.setMultiple(isMultiple);

                beneficiaires.add(beneficiaire);
            } else {
                System.out.println("Bénéficiaire déjà existant: " + existingBeneficiaire.get());
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println("Erreur lors de l'importation: " + e.getMessage());
    }

    return beneficiaires;
}

    private String getCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return null;
    }


    @Override
    public List<BeneficiareDTO> getByFilliales(String email) {
        // Récupérer l'utilisateur par email
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (!userOptional.isPresent()) {
            // Gérer le cas où l'utilisateur n'est pas trouvé
            return new ArrayList<>();
        }

        User user = userOptional.get();

        // Récupérer les filiales où l'utilisateur est responsable ou gérant
        List<Filliale> filliales = fillialeRepository.findAll().stream()
                .filter(filliale -> user.equals(filliale.getResponsable()) || user.equals(filliale.getGerant()))
                .collect(Collectors.toList());

        // Créer une liste de BeneficiareDTO pour chaque bénéficiaire des filiales trouvées
        List<BeneficiareDTO> beneficiareDTOS = new ArrayList<>();
        for (Filliale filliale : filliales) {
            for (Beneficiare beneficiare : filliale.getBeneficiares()) {
                BeneficiareDTO beneficiareDTO = new BeneficiareDTO();
                beneficiareDTO.setId(beneficiare.getId());

                beneficiareDTO.setNom(beneficiare.getNom()!=null ?beneficiare.getNom():"None");
                beneficiareDTO.setPrenom(beneficiare.getPrenom()!=null ?beneficiare.getPrenom():"None");
                beneficiareDTO.setCode(beneficiare.getCode()!=null ?beneficiare.getCode():"None");
                beneficiareDTO.setGrade(beneficiare.getGrade()!=null ?beneficiare.getGrade().name():"None");
                beneficiareDTO.setFillialeLibelle(beneficiare.getFilliale()!=null ?beneficiare.getFilliale().getLibelle():"None");
                beneficiareDTO.setMultiple(beneficiare.getMultiple());
                beneficiareDTO.setDeleted(beneficiare.getDeleted());
                beneficiareDTO.setDeletedBy(beneficiare.getDeleted_by());
                beneficiareDTO.setDateDeletion(beneficiare.getDate_deletion());
                beneficiareDTOS.add(beneficiareDTO);
            }
        }

        return beneficiareDTOS;
    }
    @Override
    public List<BeneficiareDTO> getByNumero(String numero) {
        // Récupérer le numéro
        Optional<Numero> numeroOptional = Optional.ofNullable(numeroRepository.findByNumero(numero));
        if (!numeroOptional.isPresent()) {
            // Gérer le cas où le numéro n'est pas trouvé
            return new ArrayList<>();
        }

        Numero numero1 = numeroOptional.get();

        // Récupérer la filiale associée au numéro
        Filliale filliale = numero1.getFilliale();

        // Récupérer les bénéficiaires ayant la même filiale
        List<Beneficiare> beneficiares = beneficiareRepository.findByFilliale(filliale);

        // Convertir les bénéficiaires en DTO
        List<BeneficiareDTO> beneficiareDTOs =  beneficiares.stream()
                .filter(beneficiare -> !beneficiare.getDeleted()) // Filtrer les bénéficiaires avec deleted = false
                .map(this::entityToDTO) // Convertir les bénéfic iaires restants en DTO
                .collect(Collectors.toList());

        return beneficiareDTOs;
    }

    @Override
    public List<BeneficiareDTO> getByTerminal(String imei) {
        // Récupérer le numéro
        Optional<Terminal> terminalOptional = Optional.ofNullable(terminalRepository.findByImei(imei));
        if (!terminalOptional.isPresent()) {
            // Gérer le cas où le numéro n'est pas trouvé
            return new ArrayList<>();
        }

        Terminal terminal = terminalOptional.get();

        // Récupérer la filiale associée au numéro
        Filliale filliale =terminal.getFilliale();

        // Récupérer les bénéficiaires ayant la même filiale
        List<Beneficiare> beneficiares = beneficiareRepository.findByFilliale(filliale);

        // Convertir les bénéficiaires en DTO
        List<BeneficiareDTO> beneficiareDTOs = beneficiares.stream()
                .filter(beneficiare -> !beneficiare.getDeleted())
                .filter(beneficiare -> beneficiare.getGrade().equals(terminal.getGrade()))// Filtrer les bénéficiaires avec deleted = false
                .map(this::entityToDTO) // Convertir les bénéficiaires restants en DTO
                .collect(Collectors.toList());

        return beneficiareDTOs;
    }
    @Override
    public BeneficiareDTO getByCode(String Code) {

        Beneficiare beneficiare=beneficiareRepository.findByCode(Code);
        BeneficiareDTO beneficiareDTO=new BeneficiareDTO();
        beneficiareDTO.setId(beneficiare.getId());
        beneficiareDTO.setNom(beneficiare.getNom()!=null ?beneficiare.getNom():"None");
        beneficiareDTO.setPrenom(beneficiare.getPrenom()!=null ?beneficiare.getPrenom():"None");
        beneficiareDTO.setCode(beneficiare.getCode()!=null ?beneficiare.getCode():"None");
        beneficiareDTO.setGrade(beneficiare.getGrade()!=null ?beneficiare.getGrade().name():"None");
        beneficiareDTO.setFillialeLibelle(beneficiare.getFilliale()!=null ?beneficiare.getFilliale().getLibelle():"None");
        beneficiareDTO.setMultiple(beneficiare.getMultiple());
        return beneficiareDTO;
    }

    @Override
    public BeneficiareDTO updateBeneficiare(int id, BeneficiareDTO beneficiareDTO) {
        Optional<Beneficiare> optionalBeneficiare = beneficiareRepository.findById(id);
        if (optionalBeneficiare.isPresent()) {
            Beneficiare beneficiare = optionalBeneficiare.get();

            // Vérification et mise à jour des propriétés si elles sont présentes dans le DTO
            if (beneficiareDTO.getNom() != null) {
                beneficiare.setNom(beneficiareDTO.getNom());
            }
            if (beneficiareDTO.getPrenom() != null) {
                beneficiare.setPrenom(beneficiareDTO.getPrenom());
            }
            if (beneficiareDTO.getGrade() != null) {
                beneficiare.setGrade(Grade.valueOf(beneficiareDTO.getGrade()));
            }
            if (beneficiareDTO.getFillialeLibelle() != null) {
                Filliale filliale = fillialeRepository.findByLibelle(beneficiareDTO.getFillialeLibelle());
                beneficiare.setFilliale(filliale);
            }
            if (beneficiareDTO.getMultiple() != null) {
                beneficiare.setMultiple(beneficiareDTO.getMultiple());
            }

            // Sauvegarde des changements dans le repository
            beneficiare = beneficiareRepository.save(beneficiare);

            // Conversion de l'entité mise à jour en DTO et retour
            return entityToDTO(beneficiare);
        }
        return null; // Ou lever une exception si l'id n'est pas trouvé
    }


    @Override
    public BeneficiareDTO getBeneficiareById(int id) {
        Optional<Beneficiare> beneficiare = beneficiareRepository.findById(id);
        return beneficiare.map(this::entityToDTO).orElse(null);
    }

    @Override
    public List<BeneficiareDTO> getAllBeneficiares() {
        return beneficiareRepository.findAll().stream()
                .filter(filliale -> filliale.getDeleted() == false)
                .map(this::entityToDTO).collect(Collectors.toList());
    }
    @Override
    public List<BeneficiareDTO> getHistorique() {
        return beneficiareRepository.findAll().stream()
                .filter(filliale -> filliale.getDeleted() == true)
                .map(this::entityToDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteBeneficiare(int id,String email) {
        Optional<Beneficiare> beneficiareOptional = beneficiareRepository.findById(id);


            Beneficiare beneficiare = beneficiareOptional.get();

            // Marquer le bénéficiaire comme supprimé
            beneficiare.setDeleted(true);
            beneficiare.setDeleted_by(email);
            beneficiare.setDate_deletion(String.valueOf(LocalDate.now()));  // Utiliser LocalDate.now() pour obtenir la date actuelle

            // Enregistrer les modifications dans le repository
            beneficiareRepository.save(beneficiare);


    }
    @Override
    public void recupererBeneficiare(int id) {
        Optional<Beneficiare> beneficiareOptional = beneficiareRepository.findById(id);


        Beneficiare beneficiare = beneficiareOptional.get();

        // Marquer le bénéficiaire comme supprimé
        beneficiare.setDeleted(false);


        beneficiareRepository.save(beneficiare);


    }

    private BeneficiareDTO entityToDTO(Beneficiare beneficiare) {
        BeneficiareDTO beneficiareDTO = new BeneficiareDTO();
        beneficiareDTO.setId(beneficiare.getId());
        beneficiareDTO.setNom(beneficiare.getNom()!=null ?beneficiare.getNom():"None");
        beneficiareDTO.setPrenom(beneficiare.getPrenom()!=null ?beneficiare.getPrenom():"None");
        beneficiareDTO.setCode(beneficiare.getCode()!=null ?beneficiare.getCode():"None");
        beneficiareDTO.setGrade(beneficiare.getGrade()!=null ?beneficiare.getGrade().name():"None");
        beneficiareDTO.setFillialeLibelle(beneficiare.getFilliale()!=null ?beneficiare.getFilliale().getLibelle():"None");
        beneficiareDTO.setMultiple(beneficiare.getMultiple());
        return beneficiareDTO;
    }
    @Override
    public int countBeneficiaire(String libelle) {
        // Find the filliale by its libelle
        Filliale filliale = fillialeRepository.findByLibelle(libelle);

        if (filliale == null) {
            // Handle case where the filliale is not found, e.g., return 0 or throw an exception
            return 0;
        }

        // Count the number of beneficiaires that are not deleted and belong to the specified filliale
        return (int) beneficiareRepository.countByFillialeAndDeletedFalse(filliale);
    }




}
