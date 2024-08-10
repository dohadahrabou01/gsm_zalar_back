package com.gsm_zalar.Services;

import com.gsm_zalar.DTO.FillialeDTO;
import com.gsm_zalar.Models.Filliale;

import java.util.List;

public interface FillilaleService {
    List<FillialeDTO> getAllFilliales();
    Filliale getFillialeById(int id);
    Filliale createFilliale(Filliale filliale);
    Filliale updateFilliale(int id, Filliale filliale);
    void deleteFilliale(int id,String email);
   List<FillialeDTO> getRSIFilliales();
    List<FillialeDTO> getEmailFilliales(String Email);
    List<FillialeDTO> getSIRSIFilliales(String email);
    List<FillialeDTO> getSIFilliales();
    List<FillialeDTO> getAllHistorique();
    void recuperer(int id);
}
