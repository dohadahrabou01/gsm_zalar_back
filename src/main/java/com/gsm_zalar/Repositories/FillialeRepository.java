package com.gsm_zalar.Repositories;

import com.gsm_zalar.Models.Filliale;
import com.gsm_zalar.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface FillialeRepository extends JpaRepository<Filliale,Integer> {
    Filliale findByLibelle(String libelle);

    List<Filliale> findByResponsable(Optional<User> user);

    Filliale findById(int id);

    List<Filliale> findByResponsable(User user);

    Optional<Object> findByIsDeletedFalse();

    Filliale findByGerant(User user);
}
