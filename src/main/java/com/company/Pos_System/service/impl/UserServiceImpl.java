package com.company.Pos_System.service.impl;

import com.company.Pos_System.config.JwtTokenUtil;
import com.company.Pos_System.config.UserPrincipal;
import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.LoginResponseDto;
import com.company.Pos_System.dto.TokenRequestDto;
import com.company.Pos_System.dto.UserDto;
import com.company.Pos_System.models.Users;
import com.company.Pos_System.repository.UserRepository;
import com.company.Pos_System.service.UserService;
import com.company.Pos_System.service.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public HttpApiResponse<UserDto> registerUser(UserDto dto) {
        Optional<Users> optionalUsers = userRepository.findByUsernameAndDeletedAtIsNull(dto.getUsername());
        if (optionalUsers.isPresent()) {
            return HttpApiResponse.<UserDto>builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Username already exists")
                    .build();
        }
        dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        Users user = this.userMapper.ToEntity(dto);

        this.userRepository.saveAndFlush(user);

        return HttpApiResponse.<UserDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .message("User registered successfully")
                .data(this.userMapper.ToDto(user))
                .build();
    }

    @Override
    public HttpApiResponse<LoginResponseDto> userLogin(TokenRequestDto dto) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

            // Get the UserPrincipal and extract the actual Users entity
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Users user = userPrincipal.getUser();

            // Generate token
            String token = jwtTokenUtil.getJwtToken(user.getUsername());

            // Prepare response DTO
            LoginResponseDto responseDto = LoginResponseDto.builder()
                    .token(token)
                    .user(UserDto.builder()
                            .id(user.getId())
                            .fullName(user.getFullName())
                            .username(user.getUsername())
                            .role(user.getRole())
                            .build())
                    .build();

            return HttpApiResponse.<LoginResponseDto>builder()
                    .status(HttpStatus.OK)
                    .success(true)
                    .message("Login successful")
                    .data(responseDto)
                    .build();

        } catch (AuthenticationException e) {
            return HttpApiResponse.<LoginResponseDto>builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .message("Invalid username or password")
                    .build();
        }
    }


    @Override
    public HttpApiResponse<UserDto> getUserById(Long id) {
        Users user = userRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));

        UserDto dto = userMapper.ToDto(user);

        return HttpApiResponse.<UserDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("OK")
                .data(dto)
                .build();
    }

    @Override
    public HttpApiResponse<List<UserDto>> findUsersByUsername(String username) {

        List<Users> usersList = this.userRepository.findByUsernameContainingAndDeletedAtIsNull(username).orElseThrow(
                () -> new UsernameNotFoundException("Users not found"));

        List<UserDto> userDtoList = this.userMapper.ToDtoList(usersList);

        return HttpApiResponse.<List<UserDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("OK")
                .data(userDtoList)
                .build();
    }

    @Override
    public HttpApiResponse<List<UserDto>> getAllUsers() {
        List<Users> usersList = userRepository.findAllByDeletedAtIsNull().orElseThrow((
                () -> new EntityNotFoundException("Users not found")));
        List<UserDto> userDtoList = this.userMapper.ToDtoList(usersList);
        return HttpApiResponse.<List<UserDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("OK")
                .data(userDtoList)
                .build();
    }

    @Override
    public HttpApiResponse<UserDto> updateUser(Long id, UserDto dto) {
        Users user = this.userRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));

        var updatedUser = this.userMapper.updateEntity(user, dto);

        userRepository.saveAndFlush(updatedUser);

        return HttpApiResponse.<UserDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("User updated successfully")
                .data(this.userMapper.ToDto(updatedUser))
                .build();
    }

    @Override
    public HttpApiResponse<UserDto> updateUserRole(Long id, UserDto dto) {
        Users user = this.userRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));

        var updatedUser = this.userMapper.updateEntity(user, dto);

        userRepository.saveAndFlush(updatedUser);

        return HttpApiResponse.<UserDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("User updated successfully")
                .data(this.userMapper.ToDto(updatedUser))
                .build();

    }

    @Override
    public HttpApiResponse<String> deleteUserById(Long id) {
        Users user = this.userRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));
        user.setDeletedAt(LocalDateTime.now());

        this.userRepository.saveAndFlush(user);

        return HttpApiResponse.<String>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("User deleted successfully")
                .build();
    }
}
