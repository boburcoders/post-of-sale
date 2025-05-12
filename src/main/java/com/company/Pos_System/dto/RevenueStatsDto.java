package com.company.Pos_System.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevenueStatsDto {
    private BigDecimal totalRevenue;
    private BigDecimal revenueGrowth;
    private String indicator;
}
