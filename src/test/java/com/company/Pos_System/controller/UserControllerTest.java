package com.company.Pos_System.controller;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.UserDto;
import com.company.Pos_System.models.enums.UserRole;
import com.company.Pos_System.models.Users;
import com.company.Pos_System.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    private static final String USERNAME = "BOBUR_ADMIN";
    private static final String PASSWORD = "Admin@123";

    Users user;
    UserDto userDto;

    @BeforeEach
    void setUp() {
        user = Users.builder()
                .fullName("Full Name")
                .username(USERNAME)
                .password(PASSWORD)
                .role(UserRole.ADMIN)
                .build();

        userDto = UserDto.builder()
                .fullName("Full Name")
                .username(USERNAME)
                .password(PASSWORD)
                .role(UserRole.ADMIN)
                .build();

        HttpApiResponse<UserDto> response = HttpApiResponse.<UserDto>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(userDto)
                .build();

        when(userService.registerUser(any(UserDto.class))).thenReturn(response);
        when(userService.userLogin(any(UserDto.class))).thenReturn(response);
        when(userService.getUserById(1L)).thenReturn(response);
        when(userService.getAllUsers()).thenReturn(HttpApiResponse.<List<UserDto>>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(List.of(userDto))
                .build());
        when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(response);
    }

    private String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void registerUserSuccessTest() throws Exception {
        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN"))
                        .content(toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void userLoginSuccessTest() throws Exception {
        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void userLoginFailTest() throws Exception {
        user.setPassword("wrongPassword");
        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(user)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void getUserByIdSuccessTest() throws Exception {
        mockMvc.perform(get("/api/user/by-id/{id}", 1)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllUserSuccessTest() throws Exception {
        mockMvc.perform(get("/api/user/get-all")
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateUserSuccessTest() throws Exception {
        mockMvc.perform(put("/api/user/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(userDto))
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteUserSuccessTest() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", 1)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
