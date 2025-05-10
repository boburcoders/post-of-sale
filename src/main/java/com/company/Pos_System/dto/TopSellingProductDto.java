package com.company.Pos_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopSellingProductDto {
    private String name;
    private Integer sales;
}
