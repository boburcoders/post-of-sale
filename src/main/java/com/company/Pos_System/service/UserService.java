package com.company.Pos_System.service;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.LoginResponseDto;
import com.company.Pos_System.dto.TokenRequestDto;
import com.company.Pos_System.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    HttpApiResponse<UserDto> registerUser(UserDto dto);

    HttpApiResponse<LoginResponseDto> userLogin(TokenRequestDto dto);

    HttpApiResponse<UserDto> updateUser(Long id, UserDto dto);

    HttpApiResponse<UserDto> updateUserRole(Long id, UserDto dto);

    HttpApiResponse<String> deleteUserById(Long id);

    HttpApiResponse<UserDto> getUserById(Long id);

    HttpApiResponse<List<UserDto>> findUsersByUsername(String username);

    HttpApiResponse<List<UserDto>> getAllUsers();
}
