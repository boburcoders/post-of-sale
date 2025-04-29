package com.company.Pos_System.controller;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.ProductDto;
import com.company.Pos_System.models.Category;
import com.company.Pos_System.models.Product;
import com.company.Pos_System.service.impl.ProductServiceImpl;
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

import java.math.BigDecimal;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductServiceImpl productServiceImpl;

    private static final String USERNAME = "BOBUR_ADMIN";
    private static final String PASSWORD = "Admin@123";

    @Mock
    Category category;

    Product product;
    ProductDto productDto;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name("Category Name")
                .description("description")
                .build();

        product = Product.builder()
                .name("Product Name")
                .serial("serial")
                .price(BigDecimal.TEN)
                .description("description")
                .category(category)
                .build();

        productDto = ProductDto.builder()
                .name("Product Name")
                .serial("serial")
                .price(BigDecimal.TEN)
                .description("description")
                .categoryId(1L)
                .build();

        HttpApiResponse<ProductDto> response = HttpApiResponse.<ProductDto>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(productDto)
                .build();

        HttpApiResponse<Set<ProductDto>> getAllProductResponse = HttpApiResponse.<Set<ProductDto>>builder()
                .message("OK")
                .status(HttpStatus.OK)
                .data(Set.of(productDto))
                .build();


        when(productServiceImpl.createProduct(any(ProductDto.class))).thenReturn(response);

        when(productServiceImpl.getAllProducts()).thenReturn(getAllProductResponse);

        when(productServiceImpl.getAllProductsByCategory(anyString())).thenReturn(getAllProductResponse);

        when(productServiceImpl.getProductById(1L)).thenReturn(response);

        when(productServiceImpl.updateProductById(1L, productDto)).thenReturn(response);

        when(productServiceImpl.deleteProductById(1L)).thenReturn(
                HttpApiResponse.<String>builder()
                        .message("Product deleted successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    private String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void createProduct() throws Exception {
        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(productDto))
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void getAllProducts() throws Exception {
        mockMvc.perform(get("/api/product/get-all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllProductsByCategory() throws Exception {
        mockMvc.perform(get("/api/product/get-allBy-category/{categoryName}", "categoryName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getProductById() throws Exception {
        mockMvc.perform(get("/api/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateProductById() throws Exception {
        mockMvc.perform(put("/api/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(productDto))
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteProductById() throws Exception {
        mockMvc.perform(delete("/api/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}