package com.gsm_zalar.Services;

import com.gsm_zalar.DTO.ForfaitDTO;

import java.util.List;

public interface ForfaitService {
    ForfaitDTO createForfait(ForfaitDTO forfaitDTO);
    ForfaitDTO getForfaitById(int id);
    List<ForfaitDTO> getAllForfaits();
    ForfaitDTO updateForfait(int id, ForfaitDTO forfaitDTO);
    void deleteForfait(int id);
    List<ForfaitDTO> getHistorique();
    void recupererForfait(int id);
}
