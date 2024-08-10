package com.gsm_zalar.Repositories;

import com.gsm_zalar.Models.Beneficiare;
import com.gsm_zalar.Models.Filliale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiaireRepository extends JpaRepository<Beneficiare,Integer> {
    Optional<Beneficiare> findById(Optional<Beneficiare> beneficiaireId);

    long countByFillialeAndDeletedFalse(Filliale filliale);


    List<Beneficiare> findByFilliale(Filliale filliale);

    Beneficiare findByCode(String code);


    Optional<Beneficiare> findByNomAndPrenom(String nom, String prenom);
}
