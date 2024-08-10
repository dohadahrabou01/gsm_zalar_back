package com.gsm_zalar.Services;

import com.gsm_zalar.DTO.AFNumeroDTO;
import com.gsm_zalar.Models.AFNumero;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AFNumeroService {
    AFNumero createAFNumero(String numero,int  beneficiare_id, String affectantEmail);
    List<AFNumeroDTO> getAllAFNumeroDTOs();
    AFNumeroDTO getById(int id);
    AFNumeroDTO getAFNumeroById(int id);
    void deleteAFNumero(int id,String email);
    Optional<AFNumero> getImage(int id);
    void storeFile(int id, MultipartFile file) throws IOException;
    List<AFNumeroDTO > getByEmail(String email);
    void recupererAFNumero(int id);
    List<AFNumeroDTO> getHistorique();

}
