package com.gsm_zalar.Services;

import com.gsm_zalar.DTO.TerminalDTO;
import com.gsm_zalar.Models.Terminal;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TerminalService {
    TerminalDTO createTerminal(TerminalDTO terminalDTO);

    List<TerminalDTO> getAllTerminals();

    TerminalDTO getTerminalById(int id);

    TerminalDTO updateTerminal(int id, TerminalDTO terminalDTO);

    void deleteTerminal(int id,String email);
    List<TerminalDTO> getByFilliales(String email);
    double calculerPourcentageAffectation();
    List<TerminalDTO> getTerminalByCode(String Code);
    List<TerminalDTO> getByEmail(String email);
    List<TerminalDTO> getHistorique();
    void recupererTerminal(int id);
    List<Terminal> importFromExcel(MultipartFile file);
}
