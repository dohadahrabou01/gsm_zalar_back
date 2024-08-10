package com.gsm_zalar.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    private String prenom;
    @Column(unique = true)
    private String email;
    private String password;
    private String sessionId;
    @Getter
    private Role role;
    @OneToOne(mappedBy = "gerant")
    private Filliale fillialeGerant;
    @OneToMany(mappedBy = "responsable", cascade = CascadeType.ALL)
    private List<Filliale> filliales = new ArrayList<>();

    private Boolean deleted=false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String encryptedPassword) {
        this.password=encryptedPassword;
    }

    public void setEmail(String mail) {
        email=mail;
    }

    public void setRole(Role role) {
        this.role=role;
    }
    public String getEmail() {
        return email;
    }

    public List<Filliale> getFilliales() {
        return filliales; // Renvoie la liste des filiales associées
    }

    public boolean isDeleted() {
        if (deleted==false) return false;
        return true;
    }


}
