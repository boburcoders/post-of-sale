package com.company.Pos_System.controller;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.WareHouseDto;
import com.company.Pos_System.models.WareHouse;
import com.company.Pos_System.service.impl.WareHouseServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class WareHouseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    WareHouseServiceImpl wareHouseService;

    private static final String USERNAME = "BOBUR_ADMIN";
    private static final String PASSWORD = "Admin@123";
    private static final String ROLE = "ADMIN";

    WareHouse wareHouse;
    WareHouseDto wareHouseDto;

    @BeforeEach
    void setUp() {
        wareHouse = WareHouse.builder()
                .name("Warehouse Name")
                .location("Location")
                .build();
        wareHouseDto = WareHouseDto.builder()
                .id(1L)
                .name("Warehouse Name")
                .location("Location")
                .build();

        HttpApiResponse<WareHouseDto> response = HttpApiResponse.<WareHouseDto>builder()
                .success(true)
                .message("OK")
                .data(wareHouseDto)
                .status(HttpStatus.OK)
                .build();
        when(wareHouseService.createWareHouse(wareHouseDto)).thenReturn(response);
        when(wareHouseService.getWareHouseById(1L)).thenReturn(response);
        when(wareHouseService.updateWareHouseById(1L, wareHouseDto)).thenReturn(response);
        when(wareHouseService.deleteWareHouseById(1L)).thenReturn(HttpApiResponse.<String>builder()
                .status(HttpStatus.OK)
                .success(true)
                .message("WareHouse deleted successfully")
                .build());
        when(wareHouseService.getAllWareHouse()).thenReturn(HttpApiResponse.<List<WareHouseDto>>builder()
                .success(true)
                .message("OK")
                .status(HttpStatus.OK)
                .data(List.of(wareHouseDto))
                .build());
    }

    private String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void createWareHouse() throws Exception {
        mockMvc.perform(post("/api/wareHouse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(wareHouseDto))
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getWareHouseById() throws Exception {
        mockMvc.perform(get("/api/wareHouse/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllWareHouses() throws Exception {
        mockMvc.perform(get("/api/wareHouse/get-all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateWareHouseById() throws Exception {
        mockMvc.perform(put("/api/wareHouse/{id}", 1)
                        .content(toJson(wareHouseDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteWareHouseById() throws Exception {
        mockMvc.perform(delete("/api/wareHouse/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}