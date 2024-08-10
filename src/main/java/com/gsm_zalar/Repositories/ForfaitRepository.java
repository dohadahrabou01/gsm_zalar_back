package com.gsm_zalar.Repositories;

import com.gsm_zalar.Models.Forfait;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForfaitRepository extends JpaRepository<Forfait,Integer> {
    Forfait findByLibelle(String forfait);
}
