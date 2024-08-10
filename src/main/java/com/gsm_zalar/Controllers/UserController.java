package com.gsm_zalar.Controllers;

import com.gsm_zalar.DTO.UserDTO;
import com.gsm_zalar.Models.User;
import com.gsm_zalar.Repositories.PendingUserUpdateRepository;
import com.gsm_zalar.Repositories.UserRepository;
import com.gsm_zalar.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PendingUserUpdateRepository pendingUserUpdateRepository;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserDTO user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }
    @GetMapping("/ByEmail")
    public ResponseEntity<List<UserDTO>> getByEmail(String email) {
        List<UserDTO> users =userService.getByEmail(email);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email ){
        UserDTO user = userService.getUserByEmail(email) ;
        return ResponseEntity.ok(user);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id){
        UserDTO user = userService.getUserById(id) ;
        return ResponseEntity.ok(user);
    }

    @GetMapping("/role/{email}")
    public ResponseEntity<String> getRoleByEmail(@PathVariable String email) {
        String role = userService.getRoleByEmail(email);
        return ResponseEntity.ok(role);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/historique")
    public ResponseEntity<List<UserDTO>> getHistorique() {
        List<UserDTO> users = userService.getHistorique();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody UserDTO userDTO) {
        // Set the user ID in the DTO (assuming the DTO doesn't have it)
        userDTO.setId(id);

        try {
            // Call the service to update the user
            User updatedUser = userService.updateUser(id,userDTO);
            if (updatedUser != null) {
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping ("/recuperer/{id}")
    public ResponseEntity<Void> recupererUser(@PathVariable int id) {
        userService.recupererUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String email) {
        boolean exists = userService.emailExists(email);
        if (exists) {
            return ResponseEntity.ok("Email exists");
        } else {
            return ResponseEntity.status(404).body("Email not found");
        }
    }
}
