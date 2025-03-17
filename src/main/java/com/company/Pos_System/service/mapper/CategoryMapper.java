package com.company.Pos_System.service.mapper;

import com.company.Pos_System.dto.CategoryDto;
import com.company.Pos_System.models.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    private final ProductMapper productMapper;

    public Category toEntity(CategoryDto dto) {
        return Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public CategoryDto toDto(Category entity) {
        return CategoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    public Category updateEntity(Category entity, CategoryDto dto) {
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        return entity;
    }

    public List<CategoryDto> toDtoList(List<Category> entityList) {
        List<CategoryDto> dtoList = new ArrayList<>();
        for (Category entity : entityList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }
}
