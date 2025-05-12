package com.company.Pos_System.controller;

import com.company.Pos_System.dto.CategoryDto;
import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@EnableMethodSecurity
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Create Category", description = "Create a category")
    HttpApiResponse<CategoryDto> createCategory(@RequestBody CategoryDto dto) {
        return this.categoryService.createCategory(dto);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get Category", description = "Get a category by Id")
    HttpApiResponse<CategoryDto> getCategoryById(@PathVariable Long id) {
        return this.categoryService.getCategoryById(id);
    }

    @GetMapping("/get-all")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get All Category", description = "Get Category List")
    public HttpApiResponse<List<CategoryDto>> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Update Category", description = "Update a category by Id")
    HttpApiResponse<CategoryDto> updateCategoryById(@PathVariable Long id, @RequestBody CategoryDto dto) {
        return this.categoryService.updateCategoryById(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Delete Category", description = "Delete a category by Id")
    HttpApiResponse<String> deleteCategoryById(@PathVariable Long id) {
        return this.categoryService.deleteCategoryById(id);
    }
}
