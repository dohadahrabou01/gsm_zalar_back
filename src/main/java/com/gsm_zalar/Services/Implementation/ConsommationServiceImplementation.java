package com.gsm_zalar.Services.Implementation;

import com.gsm_zalar.DTO.ConsommationDTO;
import com.gsm_zalar.Models.Consommation;
import com.gsm_zalar.Models.Numero;
import com.gsm_zalar.Repositories.ConsommationRepository;
import com.gsm_zalar.Repositories.NumeroRepository;
import com.gsm_zalar.Services.ConsommationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsommationServiceImplementation implements ConsommationService {

    @Autowired
    private ConsommationRepository consommationRepository;

    @Autowired
    private NumeroRepository numeroRepository;

    @Override
    public ConsommationDTO createConsommation(ConsommationDTO consommationDTO) {
        Numero numero = numeroRepository.findByNumero(consommationDTO.getNumero());

        if (numero == null) {
            // Log error or throw an exception
            throw new RuntimeException("Numero not found");
        }

        Consommation existingConsommation = consommationRepository.findByNumeroAndAnnee(numero, consommationDTO.getAnnee());

        if (existingConsommation != null) {
            // Log error or throw an exception
            throw new RuntimeException("Consommation for this Numero and Annee already exists");
        }

        Consommation consommation = new Consommation();
        consommation.setNumero(numero);
        consommation.setAnnee(consommationDTO.getAnnee());
        consommation.setJanvier(consommationDTO.getJanvier());
        consommation.setFevrier(consommationDTO.getFevrier());
        consommation.setMars(consommationDTO.getMars());
        consommation.setAvril(consommationDTO.getAvril());
        consommation.setMai(consommationDTO.getMai());
        consommation.setJuin(consommationDTO.getJuin());
        consommation.setJuillet(consommationDTO.getJuillet());
        consommation.setAout(consommationDTO.getAout());
        consommation.setSeptembre(consommationDTO.getSeptembre());
        consommation.setOctobre(consommationDTO.getOctobre());
        consommation.setNovembre(consommationDTO.getNovembre());
        consommation.setDecembre(consommationDTO.getDecembre());

        consommationRepository.save(consommation);

        return consommationDTO;
    }

    @Override
    public ConsommationDTO updateConsommation(int id, ConsommationDTO consommationDTO) {
        Consommation consommation = consommationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consommation not found"));


        consommation.setJanvier(consommationDTO.getJanvier());
        consommation.setFevrier(consommationDTO.getFevrier());
        consommation.setMars(consommationDTO.getMars());
        consommation.setMai(consommationDTO.getMai());
        consommation.setJuin(consommationDTO.getJuin());
        consommation.setJuillet(consommationDTO.getJuillet());
        consommation.setAout(consommationDTO.getAout());
        consommation.setSeptembre(consommationDTO.getSeptembre());
        consommation.setOctobre(consommationDTO.getOctobre());
        consommation.setNovembre(consommationDTO.getNovembre());
        consommation.setDecembre(consommationDTO.getDecembre());
        consommationRepository.save(consommation);
        return consommationDTO;
    }

    @Override
    public void deleteConsommation(int id) {
        consommationRepository.deleteById(id);
    }

    @Override
    public ConsommationDTO getConsommationById(int id) {
        Consommation consommation = consommationRepository.findById(id).orElseThrow(() -> new RuntimeException("Consommation not found"));

        ConsommationDTO consommationDTO=new ConsommationDTO();
        consommationDTO.setId(consommation.getId());
        consommationDTO.setNumero(consommation.getNumero().getNumero());
        consommationDTO.setAnnee(consommation.getAnnee());
        consommationDTO.setJanvier(consommation.getJanvier());
        consommationDTO.setFevrier(consommation.getFevrier());
        consommationDTO.setMars(consommation.getMars());
        consommationDTO.setMai(consommation.getMai());
        consommationDTO.setJuin(consommation.getJuin());
        consommationDTO.setJuillet(consommation.getJuillet());
        consommationDTO.setAout(consommation.getAout());
        consommationDTO.setSeptembre(consommation.getSeptembre());
        consommationDTO.setOctobre(consommation.getOctobre());
        consommationDTO.setNovembre(consommation.getNovembre());
        consommationDTO.setDecembre(consommation.getDecembre());
        return consommationDTO;
    }

    @Override
    public List<ConsommationDTO> getAllConsommations() {
       List<Consommation> consommations=consommationRepository.findAll();
       List<ConsommationDTO> consommationDTOS=new ArrayList<>();
       for(Consommation consommation:consommations){

           ConsommationDTO consommationDTO=new ConsommationDTO();
           consommationDTO.setId(consommation.getId());
           consommationDTO.setNumero(consommation.getNumero().getNumero());
           consommationDTO.setAnnee(consommation.getAnnee());
           consommationDTO.setJanvier(consommation.getJanvier());
           consommationDTO.setFevrier(consommation.getFevrier());
           consommationDTO.setMars(consommation.getMars());
           consommationDTO.setMai(consommation.getMai());
           consommationDTO.setJuin(consommation.getJuin());
           consommationDTO.setJuillet(consommation.getJuillet());
           consommationDTO.setAout(consommation.getAout());
           consommationDTO.setSeptembre(consommation.getSeptembre());
           consommationDTO.setOctobre(consommation.getOctobre());
           consommationDTO.setNovembre(consommation.getNovembre());
           consommationDTO.setDecembre(consommation.getDecembre());

           consommationDTOS.add(consommationDTO);
       }
           return consommationDTOS;
    }
}
