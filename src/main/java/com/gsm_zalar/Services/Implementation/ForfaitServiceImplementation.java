package com.gsm_zalar.Services.Implementation;

import com.gsm_zalar.DTO.ForfaitDTO;
import com.gsm_zalar.Models.Forfait;
import com.gsm_zalar.Repositories.ForfaitRepository;
import com.gsm_zalar.Services.ForfaitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ForfaitServiceImplementation implements ForfaitService {

    @Autowired
    private ForfaitRepository forfaitRepository;

    @Override
    public ForfaitDTO createForfait(ForfaitDTO forfaitDTO) {
        Forfait forfait = new Forfait();
        forfait.setLibelle(forfaitDTO.getLibelle());
        forfait.setPrix(forfaitDTO.getPrix());

        forfait = forfaitRepository.save(forfait);
        return convertToDTO(forfait);
    }

    @Override
    public ForfaitDTO getForfaitById(int id) {
        Forfait forfait = forfaitRepository.findById(id).orElseThrow(() -> new RuntimeException("Forfait not found"));
        return convertToDTO(forfait);
    }

    @Override
    public List<ForfaitDTO> getAllForfaits() {
        return forfaitRepository.findAll().stream()
                .filter(forfait -> !forfait.isDeleted())  // Filtrer les forfaits où deleted == false
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<ForfaitDTO> getHistorique() {
        return forfaitRepository.findAll().stream()
                .filter(forfait -> forfait.isDeleted())  // Filtrer les forfaits où deleted == false
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public ForfaitDTO updateForfait(int id, ForfaitDTO forfaitDTO) {
        Forfait existingForfait = forfaitRepository.findById(id).orElseThrow(() -> new RuntimeException("Forfait not found"));
        existingForfait.setLibelle(forfaitDTO.getLibelle());
        existingForfait.setPrix(forfaitDTO.getPrix());
        // Convert NumeroDTOs to Numeros and update them

        forfaitRepository.save(existingForfait);
        return convertToDTO(existingForfait);
    }

    @Override
    public void deleteForfait(int id) {
        Optional<Forfait> forfait=forfaitRepository.findById(id);
        forfait.get().setDeleted(true);
        forfaitRepository.save(forfait.get());


    }
    @Override
    public void recupererForfait(int id) {
        Optional<Forfait> forfait=forfaitRepository.findById(id);
        forfait.get().setDeleted(false);
        forfaitRepository.save(forfait.get());


    }

    private ForfaitDTO convertToDTO(Forfait forfait) {
        ForfaitDTO dto = new ForfaitDTO();
        dto.setId(forfait.getId());
        dto.setLibelle(forfait.getLibelle()!=null?forfait.getLibelle():"none");
        dto.setPrix(forfait.getPrix()!=null?forfait.getPrix():"-1");


        return dto;
    }
}