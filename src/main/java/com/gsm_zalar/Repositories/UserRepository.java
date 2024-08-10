package com.gsm_zalar.Repositories;

import com.gsm_zalar.Models.Role;
import com.gsm_zalar.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String username);

    List<User> findByDeletedFalse();

    User findByRole(Role role);

    List<User> findByDeletedTrue();
}
