package com.gsm_zalar.Services;

import com.gsm_zalar.DTO.BeneficiareDTO;
import com.gsm_zalar.Models.Beneficiare;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BeneficiareService {
    BeneficiareDTO createBeneficiare(BeneficiareDTO beneficiareDTO);
    BeneficiareDTO updateBeneficiare(int id, BeneficiareDTO beneficiareDTO);
    BeneficiareDTO getBeneficiareById(int id);
    List<BeneficiareDTO> getAllBeneficiares();
    List<BeneficiareDTO> getByFilliales(String email);
    void deleteBeneficiare(int id,String email);
    int countBeneficiaire(String Libelle);
    List<BeneficiareDTO> getByNumero(String numero);
    List<BeneficiareDTO> getByTerminal(String imei);
    BeneficiareDTO getByCode(String Code);
    List<BeneficiareDTO> getHistorique();
    public void recupererBeneficiare(int id);
    List<BeneficiareDTO> getByEmail(String email);
    List<Beneficiare> importFromExcel(MultipartFile file);
}
