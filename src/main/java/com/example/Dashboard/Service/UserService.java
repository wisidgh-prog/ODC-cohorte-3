package com.example.Dashboard.Service;

import com.example.Dashboard.Dto.UserRequestDTO;
import com.example.Dashboard.Dto.UserResponseDTO;
import com.example.Dashboard.Execption.DuplicateResourceException;
import com.example.Dashboard.Execption.ResourceNotFoundException;
import com.example.Dashboard.Mapper.UserMapper;
import com.example.Dashboard.Model.User;
import com.example.Dashboard.Repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Couche Service : contient la LOGIQUE METIER pour les utilisateurs.
 *
 * Utilise UserMapper pour convertir les DTOs vers les entites JPA et inversement.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    /**
     * Creer un nouvel utilisateur a partir de son DTO.
     */
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Un utilisateur avec cet email existe deja : " + requestDTO.getEmail());
        }

        User user = userMapper.toEntity(requestDTO);
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDTO(savedUser);
    }

    /**
     * Recuperer tous les utilisateurs sous forme de DTOs.
     */
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    /**
     * Recuperer un utilisateur par son identifiant sous forme de DTO.
     */
    public UserResponseDTO getUserById(Long id) {
        User user = findUserEntityById(id);
        return userMapper.toResponseDTO(user);
    }

    /**
     * Methode utilitaire interne pour recuperer l'entite User.
     */
    public User findUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec id=" + id));
    }
}
