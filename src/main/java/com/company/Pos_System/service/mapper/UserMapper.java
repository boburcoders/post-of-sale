package com.company.Pos_System.service.mapper;

import com.company.Pos_System.dto.UserDto;
import com.company.Pos_System.models.Users;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public Users ToEntity(UserDto dto) {
        return Users.builder()
                .fullName(dto.getFullName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .role(dto.getRole())
                .build();
    }

    public UserDto ToDto(Users entity) {
        return UserDto.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .username(entity.getUsername())
                .role(entity.getRole())
                .build();
    }

    public List<UserDto> ToDtoList(List<Users> entity) {
        List<UserDto> dtos = new ArrayList<>();
        for (Users user : entity) {
            dtos.add(ToDto(user));
        }
        return dtos;
    }

    public Users updateEntity(Users entity, UserDto dto) {
        if (dto.getFullName() != null) {
            entity.setFullName(dto.getFullName());
        }
        if (dto.getUsername() != null) {
            entity.setUsername(dto.getUsername());
        }
        if (dto.getPassword() != null) {
            entity.setPassword(dto.getPassword());
        }
        if (dto.getRole() != null) {
            entity.setRole(dto.getRole());
        }
        return entity;
    }
}
