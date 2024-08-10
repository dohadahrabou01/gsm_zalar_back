package com.gsm_zalar.Repositories;

import com.gsm_zalar.Models.AFNumero;
import com.gsm_zalar.Models.Beneficiare;
import com.gsm_zalar.Models.Numero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AFNumeroRepository extends JpaRepository<AFNumero,Integer> {
    List<AFNumero> findByDeletedFalse();

    List<AFNumero> findByBeneficiare(Beneficiare beneficiare);

    List<AFNumero> findByDeletedTrue();

    boolean existsByBeneficiareAndNumero(Beneficiare beneficiare, Numero numero);
}
