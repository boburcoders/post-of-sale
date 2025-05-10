package com.company.Pos_System.controller;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.ProductDto;
import com.company.Pos_System.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product")
@EnableMethodSecurity
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Create Product",description = "Create a Product with Category Id")
    HttpApiResponse<ProductDto> createProduct(@RequestBody ProductDto dto) {
        return productService.createProduct(dto);
    }

    @GetMapping("/get-all")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get All",description = "Get All Products")
    HttpApiResponse<Set<ProductDto>> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/get-allBy-category/{categoryName}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get All  by CategoryName",description = "Get All Products By Category Name")
    HttpApiResponse<Set<ProductDto>> getAllProductsByCategory(@PathVariable String categoryName) {
        return productService.getAllProductsByCategory(categoryName);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")

    @Operation(summary = "Get Product by Id",description = "Get Product with Id")
    HttpApiResponse<ProductDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Update Product by Id",description = "Update Product with Id")
    HttpApiResponse<ProductDto> updateProductById(@PathVariable Long id, @RequestBody ProductDto dto) {
        return productService.updateProductById(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Delete Product by Id",description = "Delete Product with Id")
    HttpApiResponse<String> deleteProductById(@PathVariable Long id) {
        return productService.deleteProductById(id);
    }
}
