package com.gsm_zalar.Repositories;

import com.gsm_zalar.Models.Numero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NumeroRepository extends JpaRepository<Numero,Integer> {
    Numero findByNumero(String numero);

    long countByAffectationTrue();

    long countByDeletedFalse();

    long countByAffectationTrueAndDeletedFalse();


    boolean existsByNumeroAndPukAndPin(String numero, String puk, String pin);
}
