package com.company.Pos_System.dto;

import com.company.Pos_System.models.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private Long warehouseId;
    private BigDecimal total;
    private OrderStatus status;
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    private LocalDateTime createdAt;

}
