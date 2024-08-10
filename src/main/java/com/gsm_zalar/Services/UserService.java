package com.gsm_zalar.Services;

import com.gsm_zalar.DTO.UserDTO;
import com.gsm_zalar.Models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
   User saveUser(UserDTO userDTO);
   UserDTO getUserById(int id);
   UserDTO getUserByEmail(String email) ;
   String getRoleByEmail(String Email);
   List<UserDTO> getAllUsers();
   User updateUser(int id ,UserDTO userDTO);
   void deleteUser(int id);
   List<UserDTO> getHistorique();
   UserDetailsService userDetailsService();
   boolean emailExists(String email) ;
   void recupererUser(int id);
   List<UserDTO> getByEmail(String email);
}
