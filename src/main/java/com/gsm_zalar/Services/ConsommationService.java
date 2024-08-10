package com.gsm_zalar.Services;

import com.gsm_zalar.DTO.ConsommationDTO;
import java.util.List;

public interface ConsommationService {
    ConsommationDTO createConsommation(ConsommationDTO consommationDTO);
    ConsommationDTO updateConsommation(int id, ConsommationDTO consommationDTO);
    void deleteConsommation(int id);
    ConsommationDTO getConsommationById(int id);
    List<ConsommationDTO> getAllConsommations();
}
