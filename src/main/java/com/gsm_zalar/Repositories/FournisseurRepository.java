package com.gsm_zalar.Repositories;

import com.gsm_zalar.Models.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur,Integer> {
    Fournisseur findByLibelle(String fournisseur);
    List<Fournisseur> findByDeletedFalse();

    List<Fournisseur> findByDeletedTrue();
}
