package com.gsm_zalar.Repositories;

import com.gsm_zalar.Models.Consommation;
import com.gsm_zalar.Models.Numero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsommationRepository extends JpaRepository<Consommation,Integer> {
    Consommation findByNumero(Numero num);

    Consommation findByNumeroAndAnnee(Numero numero, String annee);
}
