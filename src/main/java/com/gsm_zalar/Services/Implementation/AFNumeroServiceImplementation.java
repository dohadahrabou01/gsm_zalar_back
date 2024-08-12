package com.gsm_zalar.Services.Implementation;

import com.gsm_zalar.DTO.AFNumeroDTO;
import com.gsm_zalar.Models.*;
import com.gsm_zalar.Repositories.*;
import com.gsm_zalar.Services.AFNumeroService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AFNumeroServiceImplementation implements AFNumeroService{
    @Autowired
    private AFNumeroRepository afNumeroRepository;
    @Autowired
    private NumeroRepository numeroRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BeneficiaireRepository beneficiareRepository;
    @Autowired
    private FillialeRepository fillialeRepository;
    @Override
    public AFNumero createAFNumero(String numero, int beneficiare_id, String affectantEmail) {
        // Trouvez le bénéficiaire
        Beneficiare beneficiaire = beneficiareRepository.findById(beneficiare_id)
                .orElseThrow(() -> new RuntimeException("Bénéficiaire non trouvé"));

        // Trouvez le numéro
        Numero numero1 = numeroRepository.findByNumero(numero);
        if (numero1 == null) {
            throw new RuntimeException("Numéro non trouvé");
        }
        if (numero1.getForfait() == null) {
            throw new RuntimeException("ce numero n'a pas de forfait");
        }

        // Vérifiez si le numéro est déjà affecté
        if (numero1.getAffectation()) {
            throw new RuntimeException("Le numéro est déjà affecté");
        }

        // Trouvez l'affectant
        User affectant = userRepository.findByEmail(affectantEmail);
        if (affectant == null) {
            throw new RuntimeException("Affectant non trouvé");
        }

        // Vérifiez si le bénéficiaire peut prendre ce numéro
        List<AFNumero> existingAffectations = afNumeroRepository.findByBeneficiare(beneficiaire);
        boolean hasCard = !existingAffectations.isEmpty();
        for( AFNumero afnumero:existingAffectations){
            if(afnumero.getDeleted()!=false){
        if (!beneficiaire.isMultiple() && hasCard  ) {
            throw new RuntimeException("Le bénéficiaire ne peut pas avoir plusieurs cartes SIM");
        }}}

        System.out.println(beneficiaire.isMultiple());
        AFNumero afNumero = new AFNumero();
        afNumero.setNumero(numero1);
        afNumero.setBeneficiare(Optional.ofNullable(beneficiaire));
        afNumero.setAffectant(affectant);

        // Définir la date d'affectation actuelle
        LocalDate currentDate = LocalDate.now();
        afNumero.setDate_affectation(currentDate.format(DateTimeFormatter.ISO_DATE));

        // Marquer le numéro comme affecté
        numero1.setAffectation(true);
        numeroRepository.save(numero1);

        return afNumeroRepository.save(afNumero);
    }

    @Override
    public void storeFile(int id,MultipartFile file) throws IOException {
        Optional<AFNumero> afNumeroOptional = afNumeroRepository.findById(id);
        AFNumero afNumero=afNumeroOptional.get();
        afNumero.setImageName(file.getOriginalFilename());
        afNumero.setImageData(file.getBytes());
        afNumeroRepository.save(afNumero);

    }
    @Override
    public Optional<AFNumero> getImage(int id) {
        return afNumeroRepository.findById(id);
    }
    @Override
    public AFNumeroDTO getAFNumeroById(int id) {
        Optional<AFNumero> afNumeroOptional = afNumeroRepository.findById(id);
        if (afNumeroOptional.isPresent()) {
            AFNumero afNumero = afNumeroOptional.get();
            AFNumeroDTO afNumeroDTO = new AFNumeroDTO();
            afNumeroDTO.setId(afNumero.getId());
            afNumeroDTO.setBeneficiareDTO(afNumero.getBeneficiare());
            afNumeroDTO.setNumeroDTO(afNumero.getNumero());
            afNumeroDTO.setAffectantEmail(afNumero.getAffectant()!=null?afNumero.getAffectant().getEmail():"None");

            afNumeroDTO.setDate_affectation(afNumero.getDate_Affectation()!=null?afNumero.getDate_Affectation():"None");

            afNumeroDTO.setImprim(afNumero.getImprim());
            return afNumeroDTO;
        }
        return null; // or throw an exception if not found
    }
    @Override
    public List<AFNumeroDTO> getAllAFNumeroDTOs() {
        List<AFNumero> afNumeros = afNumeroRepository.findByDeletedFalse();
        List<AFNumeroDTO> afNumeroDTOs = new ArrayList<>();

        for (AFNumero afNumero : afNumeros) {
            AFNumeroDTO afNumeroDTO = new AFNumeroDTO();
            afNumeroDTO.setAffectantEmail(afNumero.getAffectant().getEmail());
            afNumeroDTO.setBeneficiareDTO(afNumero.getBeneficiare());
            afNumeroDTO.setNumeroDTO(afNumero.getNumero());
            afNumeroDTO.setId(afNumero.getId());

            afNumeroDTO.setDate_affectation(afNumero.getDate_Affectation());

            afNumeroDTO.setImprim(afNumero.getImprim());

            afNumeroDTOs.add(afNumeroDTO);
        }

        return afNumeroDTOs;
    }
    @Override
    public List<AFNumeroDTO> getHistorique() {
        List<AFNumero> afNumeros = afNumeroRepository.findByDeletedTrue();
        List<AFNumeroDTO> afNumeroDTOs = new ArrayList<>();

        for (AFNumero afNumero : afNumeros) {
            AFNumeroDTO afNumeroDTO = new AFNumeroDTO();
            afNumeroDTO.setAffectantEmail(afNumero.getAffectant().getEmail());
            afNumeroDTO.setBeneficiareDTO(afNumero.getBeneficiare());
            afNumeroDTO.setNumeroDTO(afNumero.getNumero());
            afNumeroDTO.setId(afNumero.getId());

            afNumeroDTO.setDate_affectation(afNumero.getDate_Affectation());

            afNumeroDTO.setImprim(afNumero.getImprim());

            afNumeroDTOs.add(afNumeroDTO);
        }

        return afNumeroDTOs;
    }
    @Override
    public AFNumeroDTO getById(int id) {
       AFNumero afNumero = (AFNumero) afNumeroRepository.findByDeletedFalse();



            AFNumeroDTO afNumeroDTO = new AFNumeroDTO();
            afNumeroDTO.setAffectantEmail(afNumero.getAffectant().getEmail());
            afNumeroDTO.setBeneficiareDTO(afNumero.getBeneficiare());
            afNumeroDTO.setNumeroDTO(afNumero.getNumero());
            afNumeroDTO.setId(afNumero.getId());

            afNumeroDTO.setDate_affectation(afNumero.getDate_Affectation());

            afNumeroDTO.setImprim(afNumero.getImprim());




        return afNumeroDTO;
    }



    private AFNumeroDTO convertToDTO(AFNumero afNumero) {
        AFNumeroDTO dto = new AFNumeroDTO();
        dto.setId(afNumero.getId());
        dto.setBeneficiareDTO(afNumero.getBeneficiare());
        dto.setNumeroDTO(afNumero.getNumero());
        dto.setAffectantEmail(afNumero.getAffectant().getEmail());


        dto.setImprim(afNumero.getImprim());
        return dto;
    }
    @Override
    public void deleteAFNumero(int id,String email) {
        Optional<AFNumero> afNumeroOptional = afNumeroRepository.findById(id);

        Numero numero=numeroRepository.findByNumero(afNumeroOptional.get().getNumero().getNumero());
        numero.setAffectation(false);
        numeroRepository.save(numero);
        AFNumero AFNumero = afNumeroOptional.get();

        // Marquer le bénéficiaire comme supprimé
        AFNumero.setDeleted(true);
        AFNumero.setDeleted_by(email);
        AFNumero.setDate_deletion(String.valueOf(LocalDate.now()));  // Utiliser LocalDate.now() pour obtenir la date actuelle

        // Enregistrer les modifications dans le repository
        afNumeroRepository.save(AFNumero);


    }
    @Override
    public void recupererAFNumero(int id) {
        Optional<AFNumero> afNumeroOptional = afNumeroRepository.findById(id);

        Numero numero=numeroRepository.findByNumero(afNumeroOptional.get().getNumero().getNumero());
     if(numero.getAffectation()==false) {
         numero.setAffectation(true);
         numeroRepository.save(numero);
         AFNumero AFNumero = afNumeroOptional.get();

         // Marquer le bénéficiaire comme supprimé
         AFNumero.setDeleted(false);

         AFNumero.setDate_deletion(String.valueOf(LocalDate.now()));  // Utiliser LocalDate.now() pour obtenir la date actuelle

         // Enregistrer les modifications dans le repository
         afNumeroRepository.save(AFNumero);
     }

    }
    @Override
    public List<AFNumeroDTO > getByEmail(String email) {
        List<AFNumero> terminals = afNumeroRepository.findAll();
        List<AFNumero> terminalList = new ArrayList<>();
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
                for (AFNumero terminal :terminals) {
                    if (!terminal.getDeleted() && filliales.contains(terminal.getBeneficiare().getFilliale())) {
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
                for (AFNumero terminal :terminals) {
                    if (!terminal.getDeleted() && terminal.getBeneficiare().getFilliale().equals(filliale1)) {
                        terminalList.add(terminal);
                    }
                }
            }
        }
        List<AFNumeroDTO> afNumeroDTOs = new ArrayList<>();

        for (AFNumero afNumero : terminalList) {
            AFNumeroDTO afNumeroDTO = new AFNumeroDTO();
            afNumeroDTO.setAffectantEmail(afNumero.getAffectant().getEmail());
            afNumeroDTO.setBeneficiareDTO(afNumero.getBeneficiare());
            afNumeroDTO.setNumeroDTO(afNumero.getNumero());
            afNumeroDTO.setId(afNumero.getId());

            afNumeroDTO.setDate_affectation(afNumero.getDate_Affectation());

            afNumeroDTO.setImprim(afNumero.getImprim());

            afNumeroDTOs.add(afNumeroDTO);
        }

        return afNumeroDTOs;

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


    public boolean isAFNumeroExist(AFNumero numero) {
        // Utiliser la méthode du repository pour vérifier si le terminal existe déjà
        return afNumeroRepository.existsByBeneficiareAndNumero(
                numero.getBeneficiare(),numero.getNumero()
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

}
