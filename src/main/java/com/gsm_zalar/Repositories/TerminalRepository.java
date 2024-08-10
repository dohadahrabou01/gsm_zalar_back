package com.gsm_zalar.Repositories;

import com.gsm_zalar.Models.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerminalRepository extends JpaRepository<Terminal, Integer> {
    long countByDeletedFalse();

    long countByAffectationTrueAndDeletedFalse();


    boolean existsByImeiAndMarqueAndModel(String imei, String marque, String model);
    Terminal findByImei(String imei);


}
