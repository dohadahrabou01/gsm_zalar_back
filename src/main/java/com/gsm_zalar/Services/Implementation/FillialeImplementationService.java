package com.gsm_zalar.Services.Implementation;

import com.gsm_zalar.DTO.FillialeDTO;
import com.gsm_zalar.Models.Filliale;
import com.gsm_zalar.Models.User;
import com.gsm_zalar.Repositories.FillialeRepository;
import com.gsm_zalar.Repositories.UserRepository;
import com.gsm_zalar.Services.FillilaleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FillialeImplementationService implements FillilaleService {
    @Autowired
    private FillialeRepository fillialeRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public List<FillialeDTO> getAllFilliales() {
      return  fillialeRepository.findAll().stream()
                .filter(filliale -> filliale.getIsDeleted() == false)
                .map(filliale -> new FillialeDTO(filliale.getId(), filliale.getLibelle(), filliale.getLieu(), filliale.getImagePath()))
                .collect(Collectors.toList());
    }
    @Override
    public List<FillialeDTO> getAllHistorique() {
        return  fillialeRepository.findAll().stream()
                .filter(filliale -> filliale.getIsDeleted() == true)
                .map(filliale -> new FillialeDTO(filliale.getId(), filliale.getLibelle(), filliale.getLieu(), filliale.getImagePath()))
                .collect(Collectors.toList());
    }
    @Override
    public void recuperer(int id) {
       Filliale filliale=fillialeRepository.getById(id);
       filliale.setIsDeleted(false);
        fillialeRepository.save(filliale);
    }
    @Override
    public List<FillialeDTO> getEmailFilliales(String email) {
        // Recherche de l'utilisateur par email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("Utilisateur non trouvé pour l'email: " + email);
        }
        String roleName = user.getRole().name();

        if (roleName.equals("RSI")) {
            List<Filliale> filiales = fillialeRepository.findByResponsable(user);
            if (filiales == null || filiales.isEmpty()) {
                throw new EntityNotFoundException("Aucune filliale trouvée pour le responsable: " + email);
            }

            // Conversion des entités Filliale en DTOs FillialeDTO
            return filiales.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else if (roleName.equals("SI")) {
            Filliale filliale = fillialeRepository.findByGerant(user);
            if (filliale == null) {
                throw new EntityNotFoundException("Aucune filliale trouvée pour le gérant: " + email);
            }

            // Conversion de l'entité Filliale en DTO FillialeDTO
            FillialeDTO fillialeDTO = convertToDTO(filliale);
            return Collections.singletonList(fillialeDTO);
        } else {
            throw new IllegalArgumentException("Rôle utilisateur non supporté: " + roleName);
        }
    }

    // Méthode utilitaire pour convertir une entité Filliale en DTO FillialeDTO
    private FillialeDTO convertToDTO(Filliale filliale) {
        FillialeDTO dto = new FillialeDTO();
        // Affectation des propriétés du DTO à partir de l'entité Filliale
        dto.setId(filliale.getId());
        dto.setLibelle(filliale.getLibelle()!=null ?filliale.getLibelle():"None");
        // Autres attributs à mapper
        return dto;
    }

    @Override
    public List<FillialeDTO> getRSIFilliales() {
        return fillialeRepository.findAll().stream()
                .filter(filliale -> filliale.getResponsable() == null )
                .filter(filliale -> !filliale.getIsDeleted()) // Filter condition
                .map(filliale -> new FillialeDTO(filliale.getId(), filliale.getLibelle(), filliale.getLieu(), filliale.getImagePath()))
                .collect(Collectors.toList());
    }
    @Override
    public List<FillialeDTO> getSIFilliales() {
        return fillialeRepository.findAll().stream()
                .filter(filliale -> filliale.getGerant() == null )
                .filter(filliale -> !filliale.getIsDeleted()) // Filter condition
                .map(filliale -> new FillialeDTO(filliale.getId(), filliale.getLibelle(), filliale.getLieu(),filliale.getImagePath()))
                .collect(Collectors.toList());
    }
    @Override
    public List<FillialeDTO> getSIRSIFilliales(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("Utilisateur non trouvé pour l'email: " + email);
        }
        List<Filliale> filiales = fillialeRepository.findByResponsable(user);
        if (filiales == null || filiales.isEmpty()) {
            throw new EntityNotFoundException("Aucune filliale trouvée pour le responsable: " + email);
        }

        // Conversion des entités Filliale en DTOs FillialeDTO
        return filiales.stream()
                .filter(filliale -> filliale.getGerant() == null )
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }


    @Override
    public Filliale getFillialeById(int id) {
        return fillialeRepository.findById(id);
    }

    @Override
    public Filliale createFilliale(Filliale filliale) {


filliale.setIsDeleted(false);
        return fillialeRepository.save(filliale);
    }

    @Override
    public Filliale updateFilliale(int id, Filliale filliale) {
        if (fillialeRepository.existsById(id)) {
            filliale.setId(id);
            return fillialeRepository.save(filliale);
        }
        return null; // Ou lancer une exception
    }

    @Override
    public void deleteFilliale(int id,String email) {
        Filliale filliale=fillialeRepository.findById(id);
        filliale.setDeletedBy(email);
        filliale.setIsDeleted(true);
        filliale.setDateDeletion(String.valueOf(LocalDate.now())); ;
        fillialeRepository.save(filliale);
    }
}
