package com.gsm_zalar.Services.Implementation;

import com.gsm_zalar.DTO.AFTerminalDTO;
import com.gsm_zalar.Models.*;
import com.gsm_zalar.Repositories.*;
import com.gsm_zalar.Services.AFTerminalService;
import com.gsm_zalar.Util.EmailUtil;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AFTerminalServiceImplementation implements AFTerminalService {

    @Autowired
    private AFTerminalRepository afTerminalRepository;
    @Autowired
    private TerminalRepository terminalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private FillialeRepository fillialeRepository;
    @Autowired
    private FournisseurRepository fournisseurRepository;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private BeneficiaireRepository beneficiareRepository; // Assurez-vous d'avoir ce repository
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Override
    public AFTerminal create(String imei,int  beneficiare_id, String affectantEmail) throws Exception {
        int Duree_Max = 3;
        List<AFTerminal> afTerminals = afTerminalRepository.findByDeletedFalse();
        for (AFTerminal afTerminal : afTerminals) {
            Duree_Max = afTerminal.getDuree_max();
        }
        Optional<Beneficiare> beneficiare = beneficiareRepository.findById(beneficiare_id);
        // Trouver le bénéficiaire dans la base de données
        Optional<Terminal> Terminal = Optional.ofNullable(terminalRepository.findByImei(imei));

        if (!beneficiare.isPresent()) {
            throw new Exception("Bénéficiaire non trouvé.");
        }

        // Trouver les téléphones existants pour le bénéficiaire
        List<AFTerminal> existingTerminals = afTerminalRepository.findByBeneficiare(beneficiare.get());

        // La date actuelle
        LocalDate currentDate = LocalDate.now();
      User user1=userRepository.findByEmail(affectantEmail);
        for (AFTerminal terminal : existingTerminals) {
            System.out.println(terminal.getDureeMax());
            String dateAffectationStr = terminal.getDate_Affectation(); // Assurez-vous que c'est une chaîne au format "yyyy-MM-dd"
            if (dateAffectationStr != null) {
                LocalDate dateAffectation = LocalDate.parse(dateAffectationStr, DATE_FORMATTER);
                long yearsBetween = ChronoUnit.YEARS.between(dateAffectation, currentDate);
                System.out.println(yearsBetween);


                if (yearsBetween <= terminal.getDureeMax() && user1.getRole()==Role.RSI) {
                    if(Terminal.get().getByFournisseur()!=null){
                    Terminal.get().setAffectation(true);
                    terminalRepository.save(Terminal.get());
                    AFTerminal newTerminal1 = new AFTerminal();
                    newTerminal1.setValidation(Validation.ENCOURS);
                    newTerminal1.setDureeMax(Duree_Max);
                    newTerminal1.setTerminal(Optional.of(Terminal.get()));
                    newTerminal1.setBeneficiare(Optional.of(beneficiare.get()));

                    newTerminal1.setAffectant(userRepository.findByEmail(affectantEmail));
                    afTerminalRepository.save(newTerminal1);
                    Notification notification=new Notification();
                    notification.setMessage(newTerminal1.getBeneficiare().getNom()+" "+newTerminal1.getBeneficiare().getPrenom()+" voulais bien prendre un autre terminal mais Attention il n'a pas encore depasse la duree max " );
                    notification.setAfTerminal(newTerminal1);
                    notificationRepository.save(notification);
                    return newTerminal1;
                }}

            }
        }
        if(Terminal.get().getByFournisseur()!=null){
        Terminal.get().setAffectation(true);

        terminalRepository.save(Terminal.get());
        AFTerminal newTerminal = new AFTerminal();
        newTerminal.setDureeMax(Duree_Max);
        newTerminal.setValidation(Validation.VALIDEE);
        newTerminal.setTerminal(Optional.of(Terminal.get()));
        newTerminal.setBeneficiare(Optional.of(beneficiare.get()));
        newTerminal.setDate_affectation(currentDate.format(DateTimeFormatter.ISO_DATE));
        newTerminal.setAffectant(userRepository.findByEmail(affectantEmail));
        afTerminalRepository.save(newTerminal);
        return newTerminal;} return null;
    }
    @Override
    public void Duree_MAX(int duree) {
        List<AFTerminal> afTerminals=afTerminalRepository.findAll();
        for (AFTerminal afterminal:afTerminals){
            afterminal.setDureeMax(duree);
            afTerminalRepository.save(afterminal);
        }

    }
    @Override
    public Boolean updateAFTerminal(int id) {
        Optional<AFTerminal> afTerminalOptional = afTerminalRepository.findById(id);
            AFTerminal afTerminal = afTerminalOptional.get();
            afTerminal.setValidation(Validation.VALIDEE);

        LocalDate currentDate = LocalDate.now();
        afTerminal.setDate_affectation(currentDate.format(DateTimeFormatter.ISO_DATE));
        afTerminalRepository.save( afTerminal);

            return true;


    }
    @Override
    public Boolean rejectedAFTerminal(int id) {
        Optional<AFTerminal> afTerminalOptional = afTerminalRepository.findById(id);
        AFTerminal afTerminal = afTerminalOptional.get();
        afTerminal.setValidation(Validation.REJECTED);
        Optional<Terminal> terminal=terminalRepository.findById(afTerminal.getTerminal().getId());
        terminal.get().setAffectation(false);
        terminalRepository.save(terminal.get());

        afTerminalRepository.save( afTerminal);

        return true;


    }
    @Override
    public void storeFile(int id, MultipartFile file) throws IOException {
        Optional<AFTerminal> afNumeroOptional = afTerminalRepository.findById(id);
        AFTerminal afNumero=afNumeroOptional.get();
        afNumero.setImageName(file.getOriginalFilename());
        afNumero.setImageData(file.getBytes());
        afTerminalRepository.save(afNumero);

    }
    @Override
    public Optional<AFTerminal> getImage(int id) {
        return afTerminalRepository.findById(id);
    }
    @Override
    public List<AFTerminalDTO> getAllAFTerminal(){
        List<AFTerminal> afTerminals= afTerminalRepository.findByDeletedFalse();
        List<AFTerminalDTO> afNumeroDTOs=new ArrayList<>();
        for (AFTerminal afTerminal:afTerminals){
           AFTerminalDTO afTerminalDTO=new AFTerminalDTO();
            afTerminalDTO.setId(afTerminal.getId());
            afTerminalDTO.setBeneficiareDTO(afTerminal.getBeneficiare());
            afTerminalDTO.setTerminalDTO(afTerminal.getTerminal());
            afTerminalDTO.setDate_Affectation(afTerminal.getDate_Affectation()!=null?afTerminal.getDate_Affectation():"None");

            afTerminalDTO.setValidation(afTerminal.getValidation()!=null?afTerminal.getValidation().name():"None");
            afTerminalDTO.setImprim(afTerminal.getImprim());
            afNumeroDTOs.add(afTerminalDTO);
        }
        return afNumeroDTOs;
    }
    @Override
    public List<AFTerminalDTO> getHistorique(){
        List<AFTerminal> afTerminals= afTerminalRepository.findByDeletedTrue();
        List<AFTerminalDTO> afNumeroDTOs=new ArrayList<>();

        for (AFTerminal afTerminal:afTerminals){
            AFTerminalDTO afTerminalDTO=new AFTerminalDTO();
            afTerminalDTO.setId(afTerminal.getId());
            afTerminalDTO.setBeneficiareDTO(afTerminal.getBeneficiare());
            afTerminalDTO.setTerminalDTO(afTerminal.getTerminal());
            afTerminalDTO.setDate_Affectation(afTerminal.getDate_Affectation()!=null?afTerminal.getDate_Affectation():"None");

            afTerminalDTO.setValidation(afTerminal.getValidation()!=null?afTerminal.getValidation().name():"None");

            afTerminalDTO.setImprim(afTerminal.getImprim());
            afNumeroDTOs.add(afTerminalDTO);
        }
        return afNumeroDTOs;
    }
    @Override
    public AFTerminalDTO getById(int id){
       Optional<AFTerminal> afTerminals= afTerminalRepository.findById(id);
        AFTerminal afTerminal=afTerminals.get();
            AFTerminalDTO afTerminalDTO=new AFTerminalDTO();
            afTerminalDTO.setId(afTerminal.getId());
            afTerminalDTO.setBeneficiareDTO(afTerminal.getBeneficiare());
            afTerminalDTO.setTerminalDTO(afTerminal.getTerminal());
        afTerminalDTO.setDate_Affectation(afTerminal.getDate_Affectation()!=null?afTerminal.getDate_Affectation():"None");

        afTerminalDTO.setValidation(afTerminal.getValidation()!=null?afTerminal.getValidation().name():"None");

        afTerminalDTO.setImprim(afTerminal.getImprim());


        return afTerminalDTO;
    }
    @Override
    public void deleteAFTerminal(int id,String email) {
        Optional<AFTerminal> afTerminalOptional = afTerminalRepository.findById(id);
      Terminal terminal=terminalRepository.findByImei(afTerminalOptional.get().getTerminal().getImei());
      terminal.setAffectation(false);
        terminalRepository.save(terminal);
        AFTerminal afTerminal = afTerminalOptional.get();

        // Marquer le bénéficiaire comme supprimé
        afTerminal.setDeleted(true);
        afTerminal.setDeleted_by(email);
        afTerminal.setDate_deletion(String.valueOf(LocalDate.now()));  // Utiliser LocalDate.now() pour obtenir la date actuelle

        // Enregistrer les modifications dans le repository
        afTerminalRepository.save(afTerminal);


    }
    @Override
    public void recupererAFTerminal(int id) {
        Optional<AFTerminal> afTerminalOptional = afTerminalRepository.findById(id);
        Terminal terminal=terminalRepository.findByImei(afTerminalOptional.get().getTerminal().getImei());
        if( terminal.getAffectation()==false) {
            terminal.setAffectation(true);
            terminalRepository.save(terminal);
            AFTerminal afTerminal = afTerminalOptional.get();

            // Marquer le bénéficiaire comme supprimé
            afTerminal.setDeleted(false);



        afTerminalRepository.save(afTerminal);
        }
    }
    @Override
    public List<AFTerminalDTO> getByEmail(String email) {
        List<AFTerminal> terminals = afTerminalRepository.findAll();
        List<AFTerminal> terminalList = new ArrayList<>();
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
                for (AFTerminal terminal :terminals) {
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
                for (AFTerminal  terminal :terminals) {
                    if (!terminal.getDeleted() && terminal.getBeneficiare().getFilliale().equals(filliale1)) {
                        terminalList.add(terminal);
                    }
                }
            }
        }
        List<AFTerminalDTO> afNumeroDTOs=new ArrayList<>();
        for (AFTerminal afTerminal:terminalList){
            AFTerminalDTO afTerminalDTO=new AFTerminalDTO();
            afTerminalDTO.setId(afTerminal.getId());
            afTerminalDTO.setBeneficiareDTO(afTerminal.getBeneficiare());
            afTerminalDTO.setTerminalDTO(afTerminal.getTerminal());
            afTerminalDTO.setDate_Affectation(afTerminal.getDate_Affectation()!=null?afTerminal.getDate_Affectation():"None");

            afTerminalDTO.setValidation(afTerminal.getValidation()!=null?afTerminal.getValidation().name():"None");

            afTerminalDTO.setImprim(afTerminal.getImprim());
            afNumeroDTOs.add(afTerminalDTO);
        }
        return afNumeroDTOs;

    }



    public boolean isAFTerminalExist(AFTerminal terminal) {
        // Utiliser la méthode du repository pour vérifier si le terminal existe déjà
        return afTerminalRepository.existsByBeneficiareAndTerminal(
                terminal.getBeneficiare(),terminal.getTerminal()
        );
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
