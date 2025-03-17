package com.company.Pos_System.controller;

import com.company.Pos_System.dto.CategoryDto;
import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Create Category", description = "Create a category")
    HttpApiResponse<CategoryDto> createCategory(@RequestBody CategoryDto dto) {
        return this.categoryService.createCategory(dto);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get Category", description = "Get a category by Id")
    HttpApiResponse<CategoryDto> getCategoryById(@PathVariable Long id) {
        return this.categoryService.getCategoryById(id);
    }

    @GetMapping("/get-all")
    @Operation(summary = "Get All Category", description = "Get Category List")
    public HttpApiResponse<List<CategoryDto>> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Category", description = "Update a category by Id")
    HttpApiResponse<CategoryDto> updateCategoryById(@PathVariable Long id, @RequestBody CategoryDto dto) {
        return this.categoryService.updateCategoryById(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Category", description = "Delete a category by Id")
    HttpApiResponse<CategoryDto> deleteCategoryById(@PathVariable Long id) {
        return this.categoryService.deleteCategoryById(id);
    }
}
