package com.gsm_zalar.Repositories;

import com.gsm_zalar.Models.AFTerminal;
import com.gsm_zalar.Models.Beneficiare;
import com.gsm_zalar.Models.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AFTerminalRepository extends JpaRepository<AFTerminal,Integer> {




    List<AFTerminal> findByBeneficiare(Beneficiare beneficiare);

    List<AFTerminal> findByDeletedFalse();

    List<AFTerminal> findByDeletedTrue();

    boolean existsByBeneficiareAndTerminal(Beneficiare beneficiare, Terminal terminal);
}
