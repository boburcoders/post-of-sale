package com.company.Pos_System.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInventoryDto {
    private Long id;
    private Long productId;
    private Long warehouseId;

    private int quantity; // number of items in stock
}
