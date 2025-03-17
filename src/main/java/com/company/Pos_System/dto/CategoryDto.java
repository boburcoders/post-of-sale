package com.company.Pos_System.dto;

import com.company.Pos_System.models.BaseEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto extends BaseEntity {
    private Long id;
    private String name;
    private String description;
    private Set<ProductDto> products = new HashSet<>();
}
