package com.company.Pos_System.controller;

import com.company.Pos_System.dto.CategoryDto;
import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.models.Category;
import com.company.Pos_System.service.impl.CategoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CategoryServiceImpl categoryService;

    private static final String USERNAME = "BOBUR_ADMIN";
    private static final String PASSWORD = "Admin@123";

    Category category;
    CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name("Category Name")
                .description("description")
                .build();
        categoryDto = CategoryDto.builder()
                .name("Category Name")
                .description("description")
                .build();

        HttpApiResponse<CategoryDto> response = HttpApiResponse.<CategoryDto>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(categoryDto)
                .build();

        HttpApiResponse<List<CategoryDto>> getAllCategoryResponse = HttpApiResponse.<List<CategoryDto>>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(List.of(categoryDto))
                .build();

        when(categoryService.createCategory(categoryDto)).thenReturn(response);
        when(categoryService.getCategoryById(anyLong())).thenReturn(response);
        when(categoryService.updateCategoryById(1L, categoryDto)).thenReturn(response);
        when(categoryService.deleteCategoryById(1L)).thenReturn(HttpApiResponse.<String>builder()
                .message("Category deleted successfully")
                .status(HttpStatus.OK)
                .build());
        when(categoryService.getAllCategory()).thenReturn(getAllCategoryResponse);
    }

    private String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void createCategory() throws Exception {
        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(categoryDto))
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getCategoryById() throws Exception {
        mockMvc.perform(get("/api/category/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllCategory() throws Exception {
        mockMvc.perform(get("/api/product/get-all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateCategoryById() throws Exception{
        mockMvc.perform(put("/api/category/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(categoryDto))
                .with(SecurityMockMvcRequestPostProcessors.user(PASSWORD).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteCategoryById() throws Exception {
        mockMvc.perform(delete("/api/category/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}