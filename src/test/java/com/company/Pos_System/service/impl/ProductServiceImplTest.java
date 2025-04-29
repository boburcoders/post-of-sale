package com.company.Pos_System.service.impl;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.ProductDto;
import com.company.Pos_System.models.Category;
import com.company.Pos_System.models.Product;
import com.company.Pos_System.repository.CategoryRepository;
import com.company.Pos_System.repository.ProductRepository;
import com.company.Pos_System.service.mapper.ProductMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    ProductMapper productMapper;
    ProductServiceImpl productService;
    Product product;
    ProductDto productDto;
    Category category;

    @BeforeEach
    void setUp() {
        product = new Product();
        productDto = new ProductDto();
        category = new Category();
        productService = new ProductServiceImpl(productRepository, categoryRepository, productMapper);
    }

    @Test
    void createProductForSuccess() {
        productDto.setId(1L);
        productDto.setCategoryId(1L);
        when(categoryRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(category));
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        HttpApiResponse<ProductDto> response = productService.createProduct(productDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("Product created successfully", response.getMessage());

        verify(productRepository).save(product);
        verify(productMapper).toEntity(productDto);
        verify(categoryRepository).findByIdAndDeletedAtIsNull(1L);

    }


    @Test
    void createProductForFailure() {
        productDto.setId(1L);
        productDto.setCategoryId(1L);
        when(categoryRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new EntityNotFoundException("Category not found"));
        assertThrows(EntityNotFoundException.class, () -> productService.createProduct(productDto));
        verifyNoInteractions(productRepository, productMapper);

    }

    @Test
    void getProductByIdForSuccess() {
        when(productRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(product));
        HttpApiResponse<ProductDto> response = productService.getProductById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("OK", response.getMessage());

        verify(productRepository).findByIdAndDeletedAtIsNull(1L);
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void getProductByIdForFailure() {
        when(productRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new EntityNotFoundException("Product not found"));

        assertThrows(EntityNotFoundException.class, () -> productService.getProductById(1L));

        verifyNoInteractions(categoryRepository, productMapper);
    }

    @Test
    void getAllProductsForSuccess() {
        Product product1 = new Product();
        Product product2 = new Product();
        Set<Product> products = new HashSet<>();
        products.add(product1);
        products.add(product2);

        ProductDto productDto1 = new ProductDto();
        ProductDto productDto2 = new ProductDto();
        Set<ProductDto> productDtos = new HashSet<>();
        productDtos.add(productDto1);
        productDtos.add(productDto2);

        when(productRepository.findAllByDeletedAtIsNull()).thenReturn(Optional.of(products));
        when(productMapper.toDtoList(products)).thenReturn(productDtos);
        HttpApiResponse<Set<ProductDto>> response = productService.getAllProducts();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("OK", response.getMessage());
        assertEquals(productDtos, response.getData());
        verify(productRepository).findAllByDeletedAtIsNull();
        verifyNoInteractions(categoryRepository);


    }


    @Test
    void getAllProductsForFailure() {
        when(productRepository.findAllByDeletedAtIsNull()).thenThrow(new EntityNotFoundException("Products not found"));

        assertThrows(EntityNotFoundException.class, () -> productService.getAllProducts());

        verifyNoInteractions(productMapper, categoryRepository);

    }

    @Test
    void getAllProductsByCategoryForSuccess() {
        when(productRepository.findAllByCategoryName(anyString())).thenReturn(Optional.of(Set.of(product)));
        when(productMapper.toDtoList(Set.of(product))).thenReturn(Set.of(productDto));

        HttpApiResponse<Set<ProductDto>> response = productService.getAllProductsByCategory(anyString());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("OK", response.getMessage());

        verify(productRepository).findAllByCategoryName(anyString());
        verify(productMapper).toDtoList(Set.of(product));
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void getAllProductsByCategoryForFailure() {
        when(productRepository.findAllByCategoryName(anyString())).thenThrow(new EntityNotFoundException("Products not found"));

        assertThrows(EntityNotFoundException.class, () -> productService.getAllProductsByCategory(anyString()));

        verifyNoInteractions(productMapper);
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void updateProductByIdForSuccess() {
        when(productRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(product));
        when(productMapper.updateEntity(product, productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        HttpApiResponse<ProductDto> response = productService.updateProductById(1L, productDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Product updated successfully", response.getMessage());
        verify(productRepository).findByIdAndDeletedAtIsNull(1L);

        verify(productMapper).updateEntity(product, productDto);
        verifyNoInteractions(categoryRepository);

    }

    @Test
    void updateProductByIdForFailure() {
        when(productRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new EntityNotFoundException("Product not found"));

        assertThrows(EntityNotFoundException.class, () -> productService.updateProductById(1L, productDto));

        verifyNoInteractions(productMapper, categoryRepository);


    }

    @Test
    void deleteProductByIdForSuccess() {
        when(productRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(product));

        HttpApiResponse<String> response = productService.deleteProductById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Product deleted successfully", response.getMessage());

        verify(productRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(productRepository, times(1)).save(product);
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void deleteProductByIdForFailure() {
        when(productRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new EntityNotFoundException("Product not found"));

        assertThrows(EntityNotFoundException.class, () -> productService.deleteProductById(1L));

        verifyNoInteractions(productMapper, categoryRepository);
    }
}