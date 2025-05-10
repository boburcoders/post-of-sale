package com.company.Pos_System.controller;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.ProductInventoryDto;
import com.company.Pos_System.service.ProductInventoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/productInventory")
@EnableMethodSecurity
public class ProductInventoryController {
    private final ProductInventoryService productInventoryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Create a new ProductInventory", description = "Create a new ProductInventory")
    public HttpApiResponse<ProductInventoryDto> createProductInventory(@RequestBody @Valid ProductInventoryDto dto) {
        return productInventoryService.createProductInventory(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Find a ProductInventory by Id", description = "Find a  ProductInventory by Id")
    public HttpApiResponse<ProductInventoryDto> getProductInventoryById(@PathVariable Long id) {
        return productInventoryService.getProductInventoryById(id);
    }

    @GetMapping("/get-all")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get All ProductInventory", description = "Get All ProductInventory")
    public HttpApiResponse<List<ProductInventoryDto>> getAllProductInventory() {
        return productInventoryService.getAllProductInventory();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Update a ProductInventory by Id", description = "Update a  ProductInventory by Id")
    public HttpApiResponse<ProductInventoryDto> updateProductInventoryById(@PathVariable Long id, @RequestBody ProductInventoryDto dto) {
        return productInventoryService.updateProductInventoryById(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Delete a ProductInventory by Id", description = "Delete a  ProductInventory by Id")
    public HttpApiResponse<String> deleteProductInventoryById(@PathVariable Long id) {
        return productInventoryService.deleteProductInventoryById(id);
    }


}
