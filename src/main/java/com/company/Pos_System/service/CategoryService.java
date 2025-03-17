package com.company.Pos_System.service;

import com.company.Pos_System.dto.CategoryDto;
import com.company.Pos_System.dto.HttpApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    HttpApiResponse<CategoryDto> createCategory(CategoryDto dto);

    HttpApiResponse<CategoryDto> getCategoryById(Long id);

    HttpApiResponse<CategoryDto> updateCategoryById(Long id, CategoryDto dto);

    HttpApiResponse<CategoryDto> deleteCategoryById(Long id);

    HttpApiResponse<List<CategoryDto>> getAllCategory();
}
