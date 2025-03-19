package com.company.Pos_System.dto;

import com.company.Pos_System.models.Product;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {
    private Long id;
    private Long orderId;
    private Long productId;
    private BigDecimal quantity = BigDecimal.ONE;
    private BigDecimal price;

    private LocalDateTime createdAt;

}
