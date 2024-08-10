package com.gsm_zalar.Services.Implementation;

import com.gsm_zalar.DTO.NumeroDTO;
import com.gsm_zalar.Models.*;
import com.gsm_zalar.Repositories.*;
import com.gsm_zalar.Services.NumeroService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
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
public class NumeroServiceImplementation implements NumeroService {
    @Autowired
    private NumeroRepository numeroRepository;

    @Autowired
    private FillialeRepository fillialeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForfaitRepository forfaitRepository;
    @Autowired
    private BeneficiaireRepository beneficiareRepository;
    @Autowired
    private AFNumeroRepository afNumeroRepository;
    @Override
    public NumeroDTO createNumero(NumeroDTO numeroDTO) {
        Numero numero = new Numero();
        numero.setNumero(numeroDTO.getNumero());
        numero.setSerie(numeroDTO.getSerie());
        numero.setPin(numeroDTO.getPin());
        numero.setPuk(numeroDTO.getPuk());

        try {
            numero.setOperateur(Operateur.valueOf(numeroDTO.getOperateur()));
        } catch (IllegalArgumentException e) {
            // Handle invalid operateur value
            throw new IllegalArgumentException("Invalid Operateur value");
        }

        numero.setForfait(forfaitRepository.findByLibelle(numeroDTO.getForfaitLibelle()));
        numero.setFilliale(fillialeRepository.findByLibelle(numeroDTO.getFillialeLibelle()));
        Numero savedNumero = numeroRepository.save(numero);
        return entityToDTO(savedNumero);
    }

    @Override
    public List<NumeroDTO> getByFilliales(String email) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (!userOptional.isPresent()) {
            return new ArrayList<>();
        }

        User user = userOptional.get();

        List<Numero> numeros = numeroRepository.findAll().stream()
                .filter(numero -> user.equals(numero.getFilliale().getResponsable()) || user.equals(numero.getFilliale().getGerant()))
                .collect(Collectors.toList());

        return numeros.stream().map(this::entityToDTO).collect(Collectors.toList());
    }

    @Override
    public NumeroDTO updateNumero(int id, NumeroDTO numeroDTO) {
        Optional<Numero> optionalNumero = numeroRepository.findById(id);
        if (optionalNumero.isPresent()) {
            Numero numero = optionalNumero.get();

            if (numeroDTO.getNumero() != null) {
                numero.setNumero(numeroDTO.getNumero());
            }
            if (numeroDTO.getSerie() != null) {
                numero.setSerie(numeroDTO.getSerie());
            }
            if (numeroDTO.getPin() != null) {
                numero.setPin(numeroDTO.getPin());
            }
            if (numeroDTO.getPuk() != null) {
                numero.setPuk(numeroDTO.getPuk());
            }
            if (numeroDTO.getAffectation() != null) {
                numero.setAffectation(numeroDTO.getAffectation());
            }
            if (numeroDTO.getOperateur() != null) {
                try {
                    numero.setOperateur(Operateur.valueOf(numeroDTO.getOperateur()));
                } catch (IllegalArgumentException e) {
                    // Handle invalid operateur value
                    throw new IllegalArgumentException("Invalid Operateur value");
                }
            }
            if (numeroDTO.getActif() != null) {
                numero.setActif(numeroDTO.getActif());
            }
            if (numeroDTO.getForfait() != null) {
                numero.setForfait(forfaitRepository.findByLibelle(numeroDTO.getForfait()));
            }
            if (numeroDTO.getFillialeLibelle() != null) {
                Filliale filliale = fillialeRepository.findByLibelle(numeroDTO.getFillialeLibelle());
                numero.setFilliale(filliale);
            }

            numero = numeroRepository.save(numero);

            return entityToDTO(numero);
        }
        return null;
    }
    @Override
    public double calculerPourcentageAffectation() {
        long totalNumeros = numeroRepository.countByDeletedFalse();
        long numerosAffectes = numeroRepository.countByAffectationTrueAndDeletedFalse();

        if (totalNumeros == 0) {
            return 0.0;
        }

        return (double) numerosAffectes / totalNumeros * 100;
    }
   @Override
   public List<NumeroDTO> getNumeroByCode(String code){
       Beneficiare beneficiare=beneficiareRepository.findByCode(code);
       List<AFNumero> afNumeros=afNumeroRepository.findByBeneficiare(beneficiare);
       List<Numero> numeros=new ArrayList<>();
       for(AFNumero afNumero:afNumeros){
           numeros.add(afNumero.getNumero());
       }
        List<NumeroDTO> numeroDTOList=new ArrayList<>();
        for(Numero numero :numeros){
            NumeroDTO numeroDTO=new NumeroDTO();
            numeroDTO.setId(numero.getId());
            numeroDTO.setNumero(numero.getNumero() != null ? numero.getNumero():"None" );
            numeroDTO.setSerie(numero.getSerie() != null ? numero.getNumero():"None");
            numeroDTO.setPin(numero.getPin() != null ? numero.getNumero():"None");
            numeroDTO.setPuk(numero.getPuk() != null ? numero.getNumero():"None");
            numeroDTO.setAffectation(numero.getAffectation());
            numeroDTO.setActif(numero.isActif());
            numeroDTO.setOperateur(numero.getFilliale() != null? numero.getOperateur().name():"None");

            numeroDTO.setFillialeLibelle(numero.getFilliale() != null? numero.getFilliale().getLibelle():"None");


            numeroDTO.setForfait(numero.getForfait() != null ? numero.getForfait().getLibelle():"None");
            numeroDTOList.add(numeroDTO);
        }
         return numeroDTOList;
   }
    @Override
    public NumeroDTO getNumeroById(int id) {
        Optional<Numero> numero = numeroRepository.findById(id);
        return numero.map(this::entityToDTO).orElse(null);
    }

    @Override
    public List<NumeroDTO> getAllNumeros() {
        return numeroRepository.findAll().stream()
                .filter(numero -> !numero.getDeleted()) // Simplified
                .map(this::entityToDTO).collect(Collectors.toList());
    }
    @Override
    public List<NumeroDTO> getHistorique() {
        return numeroRepository.findAll().stream()
                .filter(numero -> numero.getDeleted()) // Simplified
                .map(this::entityToDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteNumero(int id, String email) {
        Optional<Numero> numeroOptional = numeroRepository.findById(id);
        if (numeroOptional.isPresent()) {
            Numero numero = numeroOptional.get();
            numero.setDeleted(true);
            numero.setDeleted_by(email);
            numero.setDate_deletion(String.valueOf(LocalDate.now()));
            numeroRepository.save(numero);
        }
    }
    @Override
    public void recupererNumero(int id) {
        Optional<Numero> numeroOptional = numeroRepository.findById(id);
        if (numeroOptional.isPresent()) {
            Numero numero = numeroOptional.get();
            numero.setDeleted(false);

            numeroRepository.save(numero);
        }
    }

    private NumeroDTO entityToDTO(Numero numero) {
        NumeroDTO numeroDTO = new NumeroDTO();

        // Map basic fields
        numeroDTO.setId(numero.getId());
        numeroDTO.setNumero(numero.getNumero() != null ? numero.getNumero():"None" );
        numeroDTO.setSerie(numero.getSerie() != null ? numero.getSerie():"None");
        numeroDTO.setPin(numero.getPin() != null ? numero.getPin():"None");
        numeroDTO.setPuk(numero.getPuk() != null ? numero.getPuk():"None");
        numeroDTO.setAffectation(numero.getAffectation());
        numeroDTO.setActif(numero.isActif());
        numeroDTO.setOperateur(numero.getFilliale() != null? numero.getOperateur().name():"None");

        numeroDTO.setFillialeLibelle(numero.getFilliale() != null? numero.getFilliale().getLibelle():"None");


        numeroDTO.setForfait(numero.getForfait() != null ? numero.getForfait().getLibelle():"None");


        return numeroDTO;
    }
    @Override
    public List<Numero> importFromExcel(MultipartFile file) {
        List<Numero> numeros = new ArrayList<>();
        List<Numero> existingnumeros = numeroRepository.findAll();

        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withDelimiter(';') // Délimiteur pour CSV
                    .withFirstRecordAsHeader());

            // Définir les en-têtes attendus
            Set<String> expectedHeaders = new HashSet<>();
            expectedHeaders.add("Numero");
            expectedHeaders.add("Serie");
            expectedHeaders.add("Pin");
            expectedHeaders.add("Puk");
            expectedHeaders.add("Operateur");
            expectedHeaders.add("Actif");
            expectedHeaders.add("Forfait");
            expectedHeaders.add("Filliale");

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
                Numero numero = new Numero();
                numero.setNumero(csvRecord.get("Numero").trim());
                numero.setSerie(csvRecord.get("Serie").trim());
                numero.setPin(csvRecord.get("Pin").trim());
                numero.setPuk(csvRecord.get("Puk").trim());

                // Valeur par défaut si null ou vide
                numero.setAffectation(false); // Valeur par défaut
                numero.setOperateur(Operateur.valueOf(csvRecord.get("Operateur").trim().toUpperCase()));

                // Conversion de "OUI" à vrai et "NON" à faux
                numero.setActif(csvRecord.get("Actif").trim().equalsIgnoreCase("OUI"));

                // Trouver ou créer les objets associés
                numero.setForfait(forfaitRepository.findByLibelle(csvRecord.get("Forfait").trim()) != null
                        ? forfaitRepository.findByLibelle(csvRecord.get("Forfait").trim())
                        : null);
                numero.setFilliale(fillialeRepository.findByLibelle(csvRecord.get("Filliale").trim()) != null
                        ? fillialeRepository.findByLibelle(csvRecord.get("Filliale").trim())
                        : null);

                if (!isNumeroExist(numero)) {
                    numeros.add(numero);
                } else {
                    System.out.println("Numero déjà existant: " + numero);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'importation: " + e.getMessage());
        }

        return numeros;
    }


    public boolean isNumeroExist(Numero numero) {
        // Utiliser la méthode du repository pour vérifier si le terminal existe déjà
        return numeroRepository.existsByNumeroAndPukAndPin(
                numero.getNumero(),numero.getPuk(),numero.getPin()
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


    private String getCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        if (cell == null) {
            return null;
        }

            return cell.getStringCellValue();


    }


    @Override
    public List<NumeroDTO> getByEmail(String email) {
        List<Numero> numeros = numeroRepository.findAll();
        List<Numero> numeroList = new ArrayList<>();
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
                for (Numero numero :numeros) {
                    if (!numero.getDeleted() && filliales.contains(numero.getFilliale())) {
                        numeroList.add(numero);
                    }
                }
            } else if (roleName.equals("SI")) {
                for (Filliale filliale : fillialeList) {
                    if (filliale.getGerant() != null && filliale.getGerant().equals(user)) {
                        filliale1 = filliale;
                        break;
                    }
                }
                for (Numero numero : numeros) {
                    if (!numero.getDeleted() && numero.getFilliale().equals(filliale1)) {
                        numeroList.add(numero);
                    }
                }
            }
        }
        return numeroList.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());

    }
}
