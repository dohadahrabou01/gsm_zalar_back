package com.gsm_zalar.Services.Implementation;

import com.gsm_zalar.DTO.FillialeDTO;
import com.gsm_zalar.DTO.UserDTO;
import com.gsm_zalar.Models.Filliale;
import com.gsm_zalar.Models.PendingUserUpdate;
import com.gsm_zalar.Models.Role;
import com.gsm_zalar.Models.User;
import com.gsm_zalar.Repositories.FillialeRepository;
import com.gsm_zalar.Repositories.PendingUserUpdateRepository;
import com.gsm_zalar.Repositories.UserRepository;
import com.gsm_zalar.Services.UserService;
import com.gsm_zalar.Util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PendingUserUpdateRepository pendingUserUpdateRepository;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private FillialeRepository fillialeRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public User saveUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            System.out.println("Email already exists: " + userDTO.getEmail());
            return null; // Or handle accordingly, e.g., throw an exception
        }
        // Check if the user has filliales to associate
        if (userDTO.getFilliales() != null && !userDTO.getFilliales().isEmpty()) {
            for (FillialeDTO fillialeDTO : userDTO.getFilliales()) {
                if (fillialeDTO.getLibelle() != null) {
                    Filliale existingFilliale = (Filliale) fillialeRepository.findByLibelle(fillialeDTO.getLibelle());


                    // Check if role is RSI and responsable is not null
                    if (userDTO.getRole().equals("RSI") && existingFilliale.getResponsable() != null) {
                        System.out.println("Responsable already exists for RSI, not adding user.");
                        return null; // Or handle accordingly
                    }

                    // Check if role is SI and gerant is not null
                    if (userDTO.getRole().equals("SI") && existingFilliale.getGerant() != null) {
                        System.out.println("Gerant already exists for SI, not adding user.");
                        return null; // Or handle accordingly
                    }
                }
            }
        }

        // Create a new User object from the DTO
        User user = new User();
        user.setNom(userDTO.getNom());
        user.setPrenom(userDTO.getPrenom());
        user.setEmail(userDTO.getEmail());

        // Encrypt the user's password
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encryptedPassword);

        // Set the role
        user.setRole(Role.valueOf(userDTO.getRole()));

        // Save the user
        User savedUser = userRepository.save(user);

        // Debugging log
        System.out.println("User saved: " + savedUser);

        // Associate filliales after user creation
        if (userDTO.getFilliales() != null && !userDTO.getFilliales().isEmpty()) {
            for (FillialeDTO fillialeDTO : userDTO.getFilliales()) {
                if (fillialeDTO.getLibelle() != null) {
                    Filliale existingFilliale = (Filliale) fillialeRepository.findByLibelle(fillialeDTO.getLibelle());


                    // Set the responsable or gerant based on role
                    if (userDTO.getRole().equals("RSI") ) {
                        existingFilliale.setResponsable(savedUser);
                    }
                    if (userDTO.getRole().equals("SI")) {
                        existingFilliale.setGerant(savedUser);
                    }

                    // Update the filliale in the database
                    fillialeRepository.save(existingFilliale);

                    // Add filliale to the user's list (if not already there)
                    savedUser.getFilliales().add(existingFilliale);
                }
            }
        } else {
            System.out.println("No filliales to associate with user.");
        }
        emailUtil.sendEmail(userDTO.getEmail(), "Bienvenue a GsmZALAR",
                "Bonjour " + userDTO.getNom()+" "+ userDTO.getPrenom() + ",\n\n" +
                        "Votre compte a ete cree avec succes.\n" +
                        "Role: " + userDTO.getRole() + "\n" +
                        "Email: " + userDTO.getEmail() + "\n" +

                        "Cordialement \n"
                       );
        return savedUser;
    }



    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return null;
        }

        // Create UserDTO and map fields from User
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setNom(user.getNom());
        userDTO.setPrenom(user.getPrenom());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole().name());

        // Map filliales
        List<FillialeDTO> fillialeDTOs = user.getFilliales().stream().map(filliale -> {
            FillialeDTO dto = new FillialeDTO();
            dto.setId(filliale.getId());
            dto.setLibelle(filliale.getLibelle());
            dto.setLieu(filliale.getLieu());
            return dto;
        }).collect(Collectors.toList());
        userDTO.setFilliales(fillialeDTOs);

        // Map gerantFilliale
        if (user.getFillialeGerant() != null) {
            FillialeDTO gerantDTO = new FillialeDTO();
            gerantDTO.setId(user.getFillialeGerant().getId());
            gerantDTO.setLibelle(user.getFillialeGerant().getLibelle());
            gerantDTO.setLieu(user.getFillialeGerant().getLieu());
            userDTO.setGerantFilliale(gerantDTO);
        }

        return userDTO;
    }
    @Override
    public UserDTO getUserById(int id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return null;
        }

        // Create UserDTO and map fields from User
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setNom(user.getNom());
        userDTO.setPrenom(user.getPrenom());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole().name());

        // Map filliales
        List<FillialeDTO> fillialeDTOs = user.getFilliales().stream().map(filliale -> {
            FillialeDTO dto = new FillialeDTO();
            dto.setId(filliale.getId());
            dto.setLibelle(filliale.getLibelle());
            dto.setLieu(filliale.getLieu());
            return dto;
        }).collect(Collectors.toList());
        userDTO.setFilliales(fillialeDTOs);

        // Map gerantFilliale
        if (user.getFillialeGerant() != null) {
            FillialeDTO gerantDTO = new FillialeDTO();
            gerantDTO.setId(user.getFillialeGerant().getId());
            gerantDTO.setLibelle(user.getFillialeGerant().getLibelle());
            gerantDTO.setLieu(user.getFillialeGerant().getLieu());
            userDTO.setGerantFilliale(gerantDTO);
        }

        return userDTO;
    }
    @Override
    public String getRoleByEmail(String email) {


      User user=userRepository.findByEmail(email);
      return user.getRole().name();

    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findByDeletedFalse();
        return users.stream()
                .filter(user -> !user.getRole().name().equals("ADMIN")) // Filter out ADMIN users
                .filter(user -> !user.isDeleted()) // Ensure the user is not marked as deleted
                .map(user -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(user.getId());
                    userDTO.setNom(user.getNom());
                    userDTO.setPrenom(user.getPrenom());
                    userDTO.setEmail(user.getEmail());
                    userDTO.setRole(user.getRole().name());

                    // Add filliales
                    List<FillialeDTO> fillialeDTOs = user.getFilliales().stream().map(filliale -> {
                        FillialeDTO dto = new FillialeDTO();
                        dto.setId(filliale.getId());
                        dto.setLibelle(filliale.getLibelle());
                        dto.setLieu(filliale.getLieu());
                        return dto;
                    }).collect(Collectors.toList());
                    userDTO.setFilliales(fillialeDTOs);

                    // Add gerantFilliale
                    if (user.getFillialeGerant() != null) {
                        FillialeDTO gerantDTO = new FillialeDTO();
                        gerantDTO.setId(user.getFillialeGerant().getId());
                        gerantDTO.setLibelle(user.getFillialeGerant().getLibelle());
                        gerantDTO.setLieu(user.getFillialeGerant().getLieu());
                        userDTO.setGerantFilliale(gerantDTO);
                    }

                    return userDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getHistorique() {
        // Récupère tous les utilisateurs, y compris ceux qui ne sont pas supprimés
        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(user -> user.isDeleted()) // Filtrer pour obtenir uniquement les utilisateurs supprimés
                .filter(user -> !user.getRole().name().equals("ADMIN")) // Assurer que l'utilisateur n'est pas marqué comme supprimé
                .map(user -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(user.getId());
                    userDTO.setNom(user.getNom());
                    userDTO.setPrenom(user.getPrenom());
                    userDTO.setEmail(user.getEmail());
                    userDTO.setRole(user.getRole().name());

                    // Ajouter les filiales
                    List<FillialeDTO> fillialeDTOs = user.getFilliales().stream().map(filliale -> {
                        FillialeDTO dto = new FillialeDTO();
                        dto.setId(filliale.getId());
                        dto.setLibelle(filliale.getLibelle());
                        dto.setLieu(filliale.getLieu());
                        return dto;
                    }).collect(Collectors.toList());
                    userDTO.setFilliales(fillialeDTOs);

                    // Ajouter le gérant de la filiale
                    if (user.getFillialeGerant() != null) {
                        FillialeDTO gerantDTO = new FillialeDTO();
                        gerantDTO.setId(user.getFillialeGerant().getId());
                        gerantDTO.setLibelle(user.getFillialeGerant().getLibelle());
                        gerantDTO.setLieu(user.getFillialeGerant().getLieu());
                        userDTO.setGerantFilliale(gerantDTO);
                    }

                    return userDTO;
                })
                .collect(Collectors.toList());
    }



    @Override
    public User updateUser(int id, UserDTO userDTO) {
        // Fetch the existing user from the repository
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        User userWithEmail = userRepository.findByEmail(userDTO.getEmail());
        if (userWithEmail != null && userWithEmail.getId() != id) {
            System.out.println("Email already exists: " + userDTO.getEmail());
            return null; // Or handle accordingly, e.g., throw an exception
        }
        if (existingUser.getRole() == Role.ADMIN) {
            // Save the pending update
            PendingUserUpdate pendingUpdate = new PendingUserUpdate();
            pendingUpdate.setUserId(id);
            pendingUpdate.setNewEmail(userDTO.getEmail());
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                pendingUpdate.setNewPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            pendingUpdate.setNewRole(userDTO.getRole());
            pendingUpdate.setNom(userDTO.getNom());
            pendingUpdate.setPrenom(userDTO.getPrenom());
            pendingUpdate.setCreatedAt(LocalDateTime.now());
            pendingUserUpdateRepository.save(pendingUpdate);

            // Send email to admin for validation
            String validationLink = "http://localhost:3000/validateUpdate/" + pendingUpdate.getId();
            String emailContent = "Bonjour Admin,\n\n" +
                    "Un utilisateur avec l'email " + userDTO.getEmail() + " a demandé une modification de son compte.\n" +
                    "Veuillez valider les modifications en cliquant sur le lien suivant dans les 24 heures:\n" +
                    validationLink + "\n\n" +
                    "Cordialement,\nVotre équipe";

            emailUtil.sendEmail(existingUser.getEmail(), "Validation de modification de compte", emailContent);

            return existingUser; // Return existing user for now
        } else {
            // Update user details
            existingUser.setNom(userDTO.getNom());
            existingUser.setPrenom(userDTO.getPrenom());
            existingUser.setEmail(userDTO.getEmail());

            // Encrypt the new password if provided
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
                existingUser.setPassword(encryptedPassword);
            }

            // Set the new role
            Role newRole = Role.valueOf(userDTO.getRole());
            existingUser.setRole(newRole);

            // Handle filliale associations
            List<Filliale> currentFilliales = existingUser.getFilliales();
            List<FillialeDTO> newFilliales = userDTO.getFilliales();
            Filliale fillialeGerant = existingUser.getFillialeGerant();

            // Remove associations with previous filliales if necessary
            if (currentFilliales != null) {
                for (Filliale filliale : currentFilliales) {
                    if (existingUser.getRole() == Role.RSI) {
                        filliale.setResponsable(null);
                    }

                    fillialeRepository.save(filliale);
                }
            }

            // If the user was previously an SI and has a fillialeGerant, disassociate it
            if (existingUser.getRole() == Role.SI && fillialeGerant != null) {
                fillialeGerant.setGerant(null);
                fillialeRepository.save(fillialeGerant);
            }

            // Associate new filliales
            if (newFilliales != null && !newFilliales.isEmpty()) {
                for (FillialeDTO fillialeDTO : newFilliales) {
                    if (fillialeDTO.getLibelle() != null) {
                        Filliale existingFilliale = fillialeRepository.findByLibelle(fillialeDTO.getLibelle());

                        if (existingFilliale != null) {
                            if (newRole == Role.RSI) {
                                existingFilliale.setResponsable(existingUser);
                            } else if (newRole == Role.SI) {
                                existingFilliale.setGerant(existingUser);
                            }

                            fillialeRepository.save(existingFilliale);

                            // Add filliale to the user's list (if not already there)
                            if (!existingUser.getFilliales().contains(existingFilliale)) {
                                existingUser.getFilliales().add(existingFilliale);
                            }
                        }
                    }
                }
            }

            // Handle fillialeGerant association for SI role
            if (newRole == Role.SI && userDTO.getFillialeGerant() != null) {
                FillialeDTO fillialeGerantDTO = userDTO.getFillialeGerant();
                if (fillialeGerantDTO.getLibelle() != null) {
                    Filliale newFillialeGerant = fillialeRepository.findByLibelle(fillialeGerantDTO.getLibelle());
                    if (newFillialeGerant != null) {
                        newFillialeGerant.setGerant(existingUser);
                        fillialeRepository.save(newFillialeGerant);
                        existingUser.setFillialeGerant(newFillialeGerant);
                    }
                }
            }
            if (userDTO.getPassword() != null) {
                emailUtil.sendEmail(userDTO.getEmail(), "Modification Compte GsmZALAR",
                        "Bonjour " + userDTO.getNom() + " " + userDTO.getPrenom() + ",\n\n" +
                                "Votre compte a ete modifie.\n" +
                                "Role: " + userDTO.getRole() + "\n" +
                                "Email: " + userDTO.getEmail() + "\n" +

                                "Cordialement \n"
                );
            } else {
                emailUtil.sendEmail(userDTO.getEmail(), "Modification Compte GsmZALAR",
                        "Bonjour " + userDTO.getNom() + " " + userDTO.getPrenom() + ",\n\n" +
                                "Votre compte a ete modifie.\n" +
                                "Role: " + userDTO.getRole() + "\n" +
                                "Email: " + userDTO.getEmail() + "\n" +

                                "Cordialement \n"
                );

            }
            existingUser.setSessionId(UUID.randomUUID().toString());
            return userRepository.save(existingUser);
        }

    }


    @Override
    public void deleteUser(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Mark the user as deleted
            user.setDeleted(true); // Assuming you have a setDeleted method in your User class

            // If the user is a responsible for a filliale, set it to null
            if (user.getFilliales() != null) {
                for (Filliale filliale : user.getFilliales()) {
                    filliale.setResponsable(null); // Assuming you have a setResponsable method in your Filliale class
                    fillialeRepository.save(filliale); // Save changes to each filliale
                }
            }

            // If the user is a gerant, set it to null
            if (user.getFillialeGerant() != null) {
                Filliale fillialeGerant = user.getFillialeGerant();
                fillialeGerant.setGerant(null); // Assuming you have a setGerant method in your Filliale class
                fillialeRepository.save(fillialeGerant); // Save changes to the filliale gerant
            }

            userRepository.save(user); // Save the updated user
            emailUtil.sendEmail(user.getEmail(), "Compte GsmZALAR Supprime ",
                    "Bonjour  " + user.getNom() + ",\n\n" +
                            "Votre Compte a ete supprime.\n" +
                            "Cordialement,\n");
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
    @Override
    public void recupererUser(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();


            user.setDeleted(false);
            userRepository.save(user);
        }




    }


    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username);

    }

    @Override
    public boolean emailExists(String email) {


        User user=userRepository.findByEmail(email);
        if(user!=null) return true;
        return false;
    }
    @Override
    public List<UserDTO> getByEmail(String email) {
        // Retrieve all users and filliales from the repositories
        List<User> users = userRepository.findAll();
        List<Filliale> fillialeList = fillialeRepository.findAll();

        // Find the user by email
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));

        // Initialize an empty list to hold filtered users
        List<User> userList = new ArrayList<>();

        // If user with given email exists
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String roleName = user.getRole().name();

            // If the user's role is RSI
            if (roleName.equals("RSI")) {
                // Filter filliales based on user and add to filliales list
                List<Filliale> filliales = fillialeList.stream()
                        .filter(filliale -> filliale.getResponsable() != null
                                && filliale.getResponsable().equals(user)
                                && filliale.getGerant() != null)
                        .collect(Collectors.toList());

                // Filter users based on the role and filliale gerants
                userList = users.stream()
                        .filter(user1 -> !user1.getDeleted() &&
                                filliales.stream()
                                        .anyMatch(filliale -> filliale.getGerant().equals(user1)))
                        .collect(Collectors.toList());
            }
        }

        // Transform userList to UserDTO list
        return userList.stream()
                .filter(user -> !user.isDeleted()) // Ensure the user is not marked as deleted
                .map(user -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(user.getId());
                    userDTO.setNom(user.getNom());
                    userDTO.setPrenom(user.getPrenom());
                    userDTO.setEmail(user.getEmail());
                    userDTO.setRole(user.getRole().name());

                    // Map filliales to FillialeDTO
                    List<FillialeDTO> fillialeDTOs = user.getFilliales().stream()
                            .map(filliale -> {
                                FillialeDTO dto = new FillialeDTO();
                                dto.setId(filliale.getId());
                                dto.setLibelle(filliale.getLibelle());
                                dto.setLieu(filliale.getLieu());
                                return dto;
                            })
                            .collect(Collectors.toList());
                    userDTO.setFilliales(fillialeDTOs);

                    // Map gerantFilliale if it exists
                    if (user.getFillialeGerant() != null) {
                        FillialeDTO gerantDTO = new FillialeDTO();
                        gerantDTO.setId(user.getFillialeGerant().getId());
                        gerantDTO.setLibelle(user.getFillialeGerant().getLibelle());
                        gerantDTO.setLieu(user.getFillialeGerant().getLieu());
                        userDTO.setGerantFilliale(gerantDTO);
                    }

                    return userDTO;
                })
                .collect(Collectors.toList());
    }

}
