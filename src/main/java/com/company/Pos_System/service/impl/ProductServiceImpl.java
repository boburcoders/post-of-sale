package com.company.Pos_System.service.impl;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.ProductDto;
import com.company.Pos_System.models.Category;
import com.company.Pos_System.models.Product;
import com.company.Pos_System.models.WareHouse;
import com.company.Pos_System.repository.CategoryRepository;
import com.company.Pos_System.repository.OrderItemRepository;
import com.company.Pos_System.repository.ProductRepository;
import com.company.Pos_System.repository.WareHouseRepository;
import com.company.Pos_System.service.ProductService;
import com.company.Pos_System.service.mapper.ProductMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final WareHouseRepository wareHouseRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public HttpApiResponse<ProductDto> createProduct(ProductDto dto) {
        if (dto == null) {
            return HttpApiResponse.<ProductDto>builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Product DTO cannot be null")
                    .build();
        }

        Category category = categoryRepository.findByIdAndDeletedAtIsNull(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + dto.getCategoryId()));
        WareHouse wareHouse = wareHouseRepository.findByIdAndDeletedAtIsNull(dto.getWarehouseId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + dto.getWarehouseId()));


        Product product = productMapper.toEntity(dto);
        product.setCategory(category);
        product.setWareHouse(wareHouse);

        if (category.getProducts() == null) {
            category.setProducts(new HashSet<>());
        }
        category.getProducts().add(product);


        Product savedProduct = productRepository.save(product);

        return HttpApiResponse.<ProductDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .message("Product created successfully")
                .data(productMapper.toDto(savedProduct))
                .build();
    }

    @Override
    public HttpApiResponse<ProductDto> getProductById(Long id) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new EntityNotFoundException("Product not found"));
        return HttpApiResponse.<ProductDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("OK")
                .data(productMapper.toDto(product))
                .build();
    }

    @Override
    public HttpApiResponse<Set<ProductDto>> getAllProducts() {
        Set<Product> productList = productRepository.findAllByDeletedAtIsNull().orElseThrow(
                () -> new EntityNotFoundException("Products not found"));

        return HttpApiResponse.<Set<ProductDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("OK")
                .data(productMapper.toDtoList(productList))
                .build();
    }

    @Override
    public HttpApiResponse<Set<ProductDto>> getAllProductsByCategory(String categoryName) {
        Set<Product> productList = productRepository.findAllByCategoryName(categoryName).orElseThrow(
                () -> new EntityNotFoundException("Products not found"));

        return HttpApiResponse.<Set<ProductDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("OK")
                .data(productMapper.toDtoList(productList))
                .build();
    }

    @Override
    public HttpApiResponse<ProductDto> updateProductById(Long id, ProductDto dto) {

        Product product = productRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new EntityNotFoundException("Product not found"));

        Product updatedEntity = productMapper.updateEntity(product, dto);

        productRepository.save(updatedEntity);

        return HttpApiResponse.<ProductDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Product updated successfully")
                .data(productMapper.toDto(updatedEntity))
                .build();
    }

    @Override
    public HttpApiResponse<String> deleteProductById(Long id) {

        Product product = productRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new EntityNotFoundException("Product not found"));

        product.setDeletedAt(LocalDateTime.now());

        productRepository.save(product);

        return HttpApiResponse.<String>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Product deleted successfully")
                .build();
    }
}
