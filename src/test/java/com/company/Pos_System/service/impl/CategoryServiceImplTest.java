package com.company.Pos_System.service.impl;

import com.company.Pos_System.dto.CategoryDto;
import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.models.Category;
import com.company.Pos_System.repository.CategoryRepository;
import com.company.Pos_System.repository.ProductRepository;
import com.company.Pos_System.service.mapper.CategoryMapper;
import com.company.Pos_System.service.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    CategoryMapper categoryMapper;

    @Mock
    ProductRepository productRepository;
    @Mock
    ProductMapper productMapper;

    Category category;
    CategoryDto categoryDto;

    CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        category = new Category();
        categoryDto = new CategoryDto();
        categoryService = new CategoryServiceImpl(categoryRepository, productRepository, productMapper, categoryMapper);
    }

    @Test
    void createCategoryForSuccess() {
        categoryDto.setName("test");
        categoryDto.setDescription("test description");
        when(categoryRepository.findByNameAndDeletedAtIsNull(anyString())).thenReturn(Optional.empty());
        when(categoryMapper.toEntity(categoryDto)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        HttpApiResponse<CategoryDto> response = categoryService.createCategory(categoryDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("Category created", response.getMessage());
        assertEquals(categoryDto, response.getData());

        verify(categoryRepository, times(1)).findByNameAndDeletedAtIsNull(anyString());
        verify(categoryMapper, times(1)).toEntity(categoryDto);
        verify(categoryMapper, times(1)).toDto(category);

    }


    @Test
    void createCategoryForFailure() {
        category.setName("test");
        categoryDto.setName("test");
        categoryDto.setDescription("test description");
        when(categoryRepository.findByNameAndDeletedAtIsNull(anyString())).thenReturn(Optional.of(category));

        HttpApiResponse<CategoryDto> response = categoryService.createCategory(categoryDto);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
        assertEquals("Category already exists", response.getMessage());
        verify(categoryRepository, times(1)).findByNameAndDeletedAtIsNull(anyString());
    }

    @Test
    void getCategoryByIdForSuccess() {
        when(categoryRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        HttpApiResponse<CategoryDto> response = categoryService.getCategoryById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("OK", response.getMessage());
        assertEquals(categoryDto, response.getData());

        verify(categoryRepository, times(1)).findByIdAndDeletedAtIsNull(anyLong());
        verify(categoryMapper, times(1)).toDto(category);
    }

    @Test
    void getCategoryByIdForFailure() {
        when(categoryRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.empty());

        HttpApiResponse<CategoryDto> response = categoryService.getCategoryById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("Category not found", response.getMessage());

        verify(categoryRepository, times(1)).findByIdAndDeletedAtIsNull(anyLong());
        verifyNoInteractions(categoryMapper);
    }

    @Test
    void getAllCategoryForSuccess() {
        when(categoryRepository.findAllByDeletedAtIsNull()).thenReturn(Optional.of(List.of(category)));
        when(categoryMapper.toDtoList(List.of(category))).thenReturn(List.of(categoryDto));

        HttpApiResponse<List<CategoryDto>> response = categoryService.getAllCategory();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(List.of(categoryDto), response.getData());

        verify(categoryRepository, times(1)).findAllByDeletedAtIsNull();
        verify(categoryMapper, times(1)).toDtoList(List.of(category));
    }

    @Test
    void getAllCategoryForFailure() {
        when(categoryRepository.findAllByDeletedAtIsNull()).thenThrow(new RuntimeException("Categories not found"));

        assertThrows(RuntimeException.class, () -> categoryService.getAllCategory());

        verify(categoryRepository, times(1)).findAllByDeletedAtIsNull();
        verifyNoInteractions(categoryMapper);

    }

    @Test
    void updateCategoryByIdForSuccess() {
        when(categoryRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(category));
        when(categoryMapper.updateEntity(category, categoryDto)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        HttpApiResponse<CategoryDto> response = categoryService.updateCategoryById(1L, categoryDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Category updated", response.getMessage());
        assertEquals(categoryDto, response.getData());

        verify(categoryRepository, times(1)).findByIdAndDeletedAtIsNull(anyLong());
        verify(categoryMapper, times(1)).updateEntity(category, categoryDto);

    }

    @Test
    void updateCategoryByIdForFailure() {
        when(categoryRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.empty());

        HttpApiResponse<CategoryDto> response = categoryService.updateCategoryById(1L, categoryDto);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("Category not found", response.getMessage());

        verify(categoryRepository, times(1)).findByIdAndDeletedAtIsNull(anyLong());
        verifyNoInteractions(categoryMapper);

    }

    @Test
    void deleteCategoryByIdForSuccess() {
        when(categoryRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        HttpApiResponse<String> response = categoryService.deleteCategoryById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Category deleted", response.getMessage());
        assertNull(response.getData());

        verify(categoryRepository, times(1)).findByIdAndDeletedAtIsNull(anyLong());
        verify(categoryRepository, times(1)).save(category);

    }

    @Test
    void deleteCategoryByIdForFailure() {
        when(categoryRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.empty());

        HttpApiResponse<String> response = categoryService.deleteCategoryById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("Category not found", response.getMessage());

        verify(categoryRepository, times(1)).findByIdAndDeletedAtIsNull(anyLong());
        verifyNoInteractions(categoryMapper);

    }
}