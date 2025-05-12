package com.company.Pos_System.service.impl;

import com.company.Pos_System.config.JwtTokenUtil;
import com.company.Pos_System.config.UserPrincipal;
import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.LoginResponseDto;
import com.company.Pos_System.dto.TokenRequestDto;
import com.company.Pos_System.dto.UserDto;
import com.company.Pos_System.models.enums.UserRole;
import com.company.Pos_System.models.Users;
import com.company.Pos_System.repository.UserRepository;
import com.company.Pos_System.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenUtil jwtTokenUtil;

    UserServiceImpl userService;

    Users users;
    UserDto userDto;
    TokenRequestDto tokenRequestDto;
    LoginResponseDto responseDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        users = new Users();
        tokenRequestDto = TokenRequestDto.builder()
                .username("username")
                .password("password")
                .build();
        responseDto = LoginResponseDto.builder()
                .user(userDto)
                .token("token")
                .build();
        userService = new UserServiceImpl(userRepository, userMapper, bCryptPasswordEncoder, authenticationManager, jwtTokenUtil);
    }

    @Test
    void registerUserForExistingUser() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .fullName("Bobur Toshniyozv")
                .username("root")
                .password("password")
                .role(UserRole.ADMIN)
                .build();
        Users existingUser = new Users();
        existingUser.setUsername("root");
        when(userRepository.findByUsernameAndDeletedAtIsNull(existingUser.getUsername())).thenReturn(Optional.of(existingUser));
        HttpApiResponse<UserDto> response = userService.registerUser(userDto);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals("Username already exists", response.getMessage());

        verify(userRepository).findByUsernameAndDeletedAtIsNull(existingUser.getUsername());
        verifyNoInteractions(userMapper, bCryptPasswordEncoder);

    }

    @Test
    void registerUserForNewUser() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .fullName("Bobur Toshniyozv")
                .username("BOBUR_ADMIN")
                .password("Admin@123")
                .role(UserRole.ADMIN)
                .build();

        Users entity = new Users();
        entity.setUsername("BOBUR_ADMIN");
        entity.setPassword("encoded-password"); // simulated encoded password

        when(userRepository.findByUsernameAndDeletedAtIsNull("BOBUR_ADMIN")).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode("Admin@123")).thenReturn("encoded-password");
        when(userMapper.ToEntity(userDto)).thenReturn(entity);
        when(userMapper.ToDto(entity)).thenReturn(userDto);
        when(userRepository.saveAndFlush(entity)).thenReturn(entity);

        HttpApiResponse<UserDto> response = userService.registerUser(userDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("User registered successfully", response.getMessage());
        assertEquals(userDto, response.getData());

        verify(userRepository).findByUsernameAndDeletedAtIsNull("BOBUR_ADMIN");
        verify(userRepository).saveAndFlush(entity);
        verify(userMapper).ToEntity(userDto);
        verify(userMapper).ToDto(entity);
        verify(bCryptPasswordEncoder).encode("Admin@123");
    }



    @WithMockUser(username = "BOBUR_ADMIN", password = "Admin@123", roles = {"ADMIN"})
    @Test
    void userLoginForSuccess() {
        // Given
        TokenRequestDto tokenRequestDto = new TokenRequestDto();
        tokenRequestDto.setUsername("BOBUR_ADMIN");
        tokenRequestDto.setPassword("Admin@123");

        Users userEntity = new Users();
        userEntity.setId(1L);
        userEntity.setFullName("Bobur Toshniyozv");
        userEntity.setUsername("BOBUR_ADMIN");
        userEntity.setPassword("Admin@123");
        userEntity.setRole(UserRole.ADMIN);

        UserDto userDto = UserDto.builder()
                .id(userEntity.getId())
                .fullName(userEntity.getFullName())
                .username(userEntity.getUsername())
                .role(userEntity.getRole())
                .build();

        UserPrincipal userPrincipal = new UserPrincipal(userEntity);

        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getPrincipal()).thenReturn(userPrincipal);

        when(authenticationManager.authenticate(any())).thenReturn(mockAuthentication);
        when(jwtTokenUtil.getJwtToken(userEntity.getUsername())).thenReturn("fake-jwt-token");

        HttpApiResponse<LoginResponseDto> response = userService.userLogin(tokenRequestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Login successful", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("fake-jwt-token", response.getData().getToken());
        assertEquals(userDto.getUsername(), response.getData().getUser().getUsername());

    }



    @Test
    void getUserByIdForSuccess() {
        Users entity = new Users();
        entity.setId(1L);
        when(userRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(entity));
        HttpApiResponse<UserDto> response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("OK", response.getMessage());

        verify(userRepository).findByIdAndDeletedAtIsNull(1L);
    }


    @Test
    void getUserByIdForFail() {
        when(userRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new UsernameNotFoundException("User not found"));
        assertThrows(UsernameNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verifyNoInteractions(userMapper, bCryptPasswordEncoder);
    }

    @Test
    void findUsersByUsernameForFail() {
        when(userRepository.findByUsernameContainingAndDeletedAtIsNull(anyString())).
                thenThrow(new UsernameNotFoundException("Users not found"));
        assertThrows(UsernameNotFoundException.class, () -> userService.findUsersByUsername(anyString()));
        verify(userRepository, times(1)).findByUsernameContainingAndDeletedAtIsNull(anyString());
        verifyNoInteractions(userMapper);
    }

    @Test
    void findUsersByUsernameForSuccess() {
        Users entity = new Users();
        entity.setId(1L);
        entity.setUsername("BOBUR_ADMIN");
        Users entity2 = new Users();
        entity2.setId(2L);
        entity2.setUsername("BOBUR_ADMIN");

        when(userRepository.findByUsernameContainingAndDeletedAtIsNull(anyString()))
                .thenReturn(Optional.of(List.of(entity, entity2)));

        HttpApiResponse<List<UserDto>> response = userService.findUsersByUsername(anyString());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("OK", response.getMessage());
        verify(userRepository).findByUsernameContainingAndDeletedAtIsNull(anyString());

    }

    @Test
    void getAllUsersForSuccess() {
        Users entity = new Users();
        entity.setId(1L);
        entity.setUsername("BOBUR_ADMIN");
        Users entity2 = new Users();
        entity2.setId(2L);
        entity2.setUsername("BOBUR_ADMIN");

        when(userRepository.findAllByDeletedAtIsNull())
                .thenReturn(Optional.of(List.of(entity, entity2)));

        HttpApiResponse<List<UserDto>> response = userService.getAllUsers();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("OK", response.getMessage());
        verify(userRepository).findAllByDeletedAtIsNull();
    }

    @Test
    void getAllUsersForFail() {
        when(userRepository.findAllByDeletedAtIsNull()).
                thenThrow(new UsernameNotFoundException("Users not found"));
        assertThrows(UsernameNotFoundException.class, () -> userService.getAllUsers());
        verify(userRepository, times(1)).findAllByDeletedAtIsNull();
        verifyNoInteractions(userMapper);
    }

    @Test
    void updateUserForSuccess() {
        when(userRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(users));
        when(userMapper.updateEntity(users, userDto)).thenReturn(users);

        HttpApiResponse<UserDto> response = userService.updateUser(1L, userDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("User updated successfully", response.getMessage());
        verify(userRepository).findByIdAndDeletedAtIsNull(1L);
        verify(userMapper).updateEntity(users, userDto);
    }


    @Test
    void updateUserForFail() {
        when(userRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new UsernameNotFoundException("User not found"));
        assertThrows(UsernameNotFoundException.class, () -> userService.updateUser(1L, userDto));
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verifyNoInteractions(userMapper);
    }


    @Test
    void deleteUserById() {
        when(userRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(users));
        HttpApiResponse<String> response = userService.deleteUserById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("User deleted successfully", response.getMessage());
        verify(userRepository).findByIdAndDeletedAtIsNull(1L);
        verifyNoInteractions(userMapper);
    }
}