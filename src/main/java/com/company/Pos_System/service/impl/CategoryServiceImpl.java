package com.company.Pos_System.service.impl;

import com.company.Pos_System.dto.CategoryDto;
import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.models.Category;
import com.company.Pos_System.repository.CategoryRepository;
import com.company.Pos_System.service.CategoryService;
import com.company.Pos_System.service.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public HttpApiResponse<CategoryDto> createCategory(CategoryDto dto) {
        Optional<Category> optionalCategory = categoryRepository.findByNameAndDeletedAtIsNull(dto.getName());
        if (optionalCategory.isPresent()) {
            return HttpApiResponse.<CategoryDto>builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Category already exists")
                    .build();
        }
        Category entity = categoryMapper.toEntity(dto);

        categoryRepository.save(entity);

        return HttpApiResponse.<CategoryDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .message("Category created")
                .data(categoryMapper.toDto(entity))
                .build();
    }

    @Override
    public HttpApiResponse<CategoryDto> getCategoryById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndDeletedAtIsNull(id);
        if (optionalCategory.isEmpty()) {
            return HttpApiResponse.<CategoryDto>builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Category not found")
                    .build();
        }
        return HttpApiResponse.<CategoryDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("OK")
                .data(categoryMapper.toDto(optionalCategory.get()))
                .build();
    }

    @Override
    public HttpApiResponse<List<CategoryDto>> getAllCategory() {
        List<Category> categoryList = categoryRepository.findAllByDeletedAtIsNull().orElseThrow(
                () -> new RuntimeException("Categories not found"));

        return HttpApiResponse.<List<CategoryDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("OK")
                .data(categoryMapper.toDtoList(categoryList))
                .build();
    }

    @Override
    public HttpApiResponse<CategoryDto> updateCategoryById(Long id, CategoryDto dto) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndDeletedAtIsNull(id);
        if (optionalCategory.isEmpty()) {
            return HttpApiResponse.<CategoryDto>builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Category not found")
                    .build();
        }
        Category updatedEntity = categoryMapper.updateEntity(optionalCategory.get(), dto);

        categoryRepository.save(updatedEntity);

        return HttpApiResponse.<CategoryDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Category updated")
                .data(categoryMapper.toDto(updatedEntity))
                .build();
    }

    @Override
    public HttpApiResponse<String> deleteCategoryById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndDeletedAtIsNull(id);
        if (optionalCategory.isEmpty()) {
            return HttpApiResponse.<String>builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Category not found")
                    .build();
        }

        optionalCategory.get().setDeletedAt(LocalDateTime.now());

        categoryRepository.save(optionalCategory.get());

        return HttpApiResponse.<String>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Category deleted")
                .build();
    }
}
