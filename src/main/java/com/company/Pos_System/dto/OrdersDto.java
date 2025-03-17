package com.company.Pos_System.dto;

import com.company.Pos_System.enums.OrderStatus;
import com.company.Pos_System.models.OrderItems;
import com.company.Pos_System.models.Users;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersDto {
    private Long id;
    private Long userId;
    private BigDecimal total;
    private OrderStatus status;
    private List<Long> orderItems = new ArrayList<>();
}
