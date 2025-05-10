package com.company.Pos_System.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalOrdersDto {
    private Long totalOrders;
    private BigDecimal ordersGrowth;
    private String indicator;
}
