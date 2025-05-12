package com.company.Pos_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalesOverviewDto {
    private String month;
    private Double revenue;
}
