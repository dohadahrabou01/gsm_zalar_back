package com.gsm_zalar.Services;

import com.gsm_zalar.DTO.AFTerminalDTO;
import com.gsm_zalar.Models.AFTerminal;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AFTerminalService {
    AFTerminal create(String imei,int  beneficiare_id, String affectantEmail) throws Exception;
    List<AFTerminalDTO> getAllAFTerminal();
    void Duree_MAX(int duree);
    Boolean updateAFTerminal(int id);
    void deleteAFTerminal(int id,String email);
    Boolean rejectedAFTerminal(int id);
    void storeFile(int id, MultipartFile file) throws IOException;
    Optional<AFTerminal> getImage(int id);
    AFTerminalDTO getById(int id);
    List<AFTerminalDTO> getByEmail(String email);
    List<AFTerminalDTO> getHistorique();
    void recupererAFTerminal(int id);

}
