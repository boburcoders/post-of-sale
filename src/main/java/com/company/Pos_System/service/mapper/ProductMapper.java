package com.company.Pos_System.service.mapper;

import com.company.Pos_System.dto.ProductDto;
import com.company.Pos_System.models.Category;
import com.company.Pos_System.models.Product;
import com.company.Pos_System.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final CategoryRepository categoryRepository;

    public Product toEntity(ProductDto dto) {
        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .serial(dto.getSerial())
                .build();
    }

    public ProductDto toDto(Product entity) {
        return ProductDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .serial(entity.getSerial())
                .createdAt(entity.getCreatedAt())
                .categoryId(entity.getCategory().getId())
                .build();
    }

    public Product updateEntity(Product entity, ProductDto dto) {
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }
        if (dto.getSerial() != null) {
            entity.setSerial(dto.getSerial());
        }
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findByIdAndDeletedAtIsNull(
                    dto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));

            category.setProducts(Set.of(entity));
            categoryRepository.save(category);
            entity.setCategory(category);
        }
        return entity;
    }

    public Set<ProductDto> toDtoList(Set<Product> entityList) {
        Set<ProductDto> dtoList = new HashSet<>();
        for (Product entity : entityList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

    public Set<Product> toEntityList(Set<ProductDto> productDtoList) {
        Set<Product> entityList = new HashSet<>();
        for (ProductDto productDto : productDtoList) {
            entityList.add(toEntity(productDto));
        }
        return entityList;
    }
}
