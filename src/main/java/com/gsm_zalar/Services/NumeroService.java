package com.gsm_zalar.Services;

import com.gsm_zalar.DTO.NumeroDTO;
import com.gsm_zalar.Models.Numero;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NumeroService {
    NumeroDTO createNumero(NumeroDTO numeroDTO);
    NumeroDTO getNumeroById(int id) ;
    List<NumeroDTO> getAllNumeros();
    NumeroDTO updateNumero(int id, NumeroDTO numeroDTO);
    void deleteNumero(int id, String email);
    List<NumeroDTO> getByFilliales(String email);
    double calculerPourcentageAffectation();
    List<NumeroDTO> getNumeroByCode(String Code);
    List<NumeroDTO> getHistorique();
    void recupererNumero(int id);
     List<NumeroDTO> getByEmail(String email);
    List<Numero> importFromExcel(MultipartFile file);
}
