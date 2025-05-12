package com.company.Pos_System.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesTodayDto {
    private BigDecimal salesToday;
    private BigDecimal salesGrowth;
    private String indicator;
}
