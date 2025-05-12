package com.company.Pos_System.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private String serial;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private Long warehouseId;

    private LocalDateTime createdAt;

}
