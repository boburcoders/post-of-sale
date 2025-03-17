package com.company.Pos_System.controller;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.ProductDto;
import com.company.Pos_System.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/{categoryId}")
    @Operation(summary = "Create Product",description = "Create a Product with Category Id")
    HttpApiResponse<ProductDto> createProduct(@PathVariable Long categoryId, @RequestBody ProductDto dto) {
        return productService.createProduct(categoryId, dto);
    }

    @GetMapping("/get-all")
    @Operation(summary = "Get All",description = "Get All Products")
    HttpApiResponse<Set<ProductDto>> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/get-allBy-category/{categoryName}")
    @Operation(summary = "Get All  by CategoryName",description = "Get All Products By Category Name")
    HttpApiResponse<Set<ProductDto>> getAllProductsByCategory(@PathVariable String categoryName) {
        return productService.getAllProductsByCategory(categoryName);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Product by Id",description = "Get Product with Id")
    HttpApiResponse<ProductDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Product by Id",description = "Update Product with Id")
    HttpApiResponse<ProductDto> updateProductById(@PathVariable Long id, @RequestBody ProductDto dto) {
        return productService.updateProductById(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Product by Id",description = "Delete Product with Id")
    HttpApiResponse<String> deleteProductById(@PathVariable Long id) {
        return productService.deleteProductById(id);
    }
}
