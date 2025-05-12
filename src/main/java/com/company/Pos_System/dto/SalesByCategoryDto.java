package com.company.Pos_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalesByCategoryDto {
    private String category;
    private Double percentage;
}
