package com.example.Dashboard.Mapper;

import com.example.Dashboard.Dto.UserRequestDTO;
import com.example.Dashboard.Dto.UserResponseDTO;
import com.example.Dashboard.Model.User;
import org.springframework.stereotype.Component;

/**
 * Mapper pour transformer les objets User en DTOs et inversement.
 * Composant Spring injectable grace a @Component.
 */
@Component
public class UserMapper {

    /**
     * Convertit un DTO de creation en entite User.
     */
    public User toEntity(UserRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }

    /**
     * Convertit une entite User en DTO de reponse.
     */
    public UserResponseDTO toResponseDTO(User entity) {
        if (entity == null) {
            return null;
        }
        return UserResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }
}
