package com.company.Pos_System.controller;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.ProductInventoryDto;
import com.company.Pos_System.models.Product;
import com.company.Pos_System.models.ProductInventory;
import com.company.Pos_System.models.WareHouse;
import com.company.Pos_System.service.impl.ProductInventoryServiceImpl;
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
class ProductInventoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductInventoryServiceImpl productInventoryService;

    private static final String USERNAME = "BOBUR_ADMIN";
    private static final String PASSWORD = "Admin@123";
    private static final String ROLE = "ADMIN";

    ProductInventory productInventory;
    ProductInventoryDto productInventoryDto;
    WareHouse wareHouse;

    @BeforeEach
    void setUp() {
        wareHouse = WareHouse.builder()
                .name("Warehouse Name")
                .location("Location")
                .build();

        productInventory = ProductInventory.builder()
                .quantity(122)
                .warehouse(wareHouse)
                .product(new Product())
                .build();
        productInventoryDto = ProductInventoryDto.builder()
                .id(1L)
                .quantity(122)
                .warehouseId(1L)
                .productId(1L)
                .build();

        HttpApiResponse<ProductInventoryDto> response = HttpApiResponse.<ProductInventoryDto>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .success(true)
                .data(productInventoryDto)
                .build();
        when(productInventoryService.createProductInventory(productInventoryDto)).thenReturn(response);
        when(productInventoryService.getProductInventoryById(1L)).thenReturn(response);
        when(productInventoryService.updateProductInventoryById(1L, productInventoryDto)).thenReturn(response);
        when(productInventoryService.deleteProductInventoryById(1L)).thenReturn(HttpApiResponse.<String>builder()
                .success(true)
                .message("ProductInventory deleted successfully")
                .status(HttpStatus.OK)
                .build());
        when(productInventoryService.getAllProductInventory()).thenReturn(HttpApiResponse.<List<ProductInventoryDto>>builder()
                .success(true)
                .message("OK")
                .status(HttpStatus.OK)
                .data(List.of(productInventoryDto))
                .build());
    }

    private String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void createProductInventory() throws Exception {
        mockMvc.perform(post("/api/productInventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(productInventoryDto))
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getProductInventoryById() throws Exception {
        mockMvc.perform(get("/api/productInventory/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllProductInventory() throws Exception {
        mockMvc.perform(get("/api/productInventory/get-all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateProductInventoryById() throws Exception {
        mockMvc.perform(put("/api/productInventory/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(productInventoryDto))
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteProductInventoryById() throws Exception {
        mockMvc.perform(delete("/api/productInventory/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}