package com.company.Pos_System.service.mapper;

import com.company.Pos_System.dto.OrdersDto;
import com.company.Pos_System.models.Order;
import com.company.Pos_System.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;

    public Order toEntity(OrdersDto dto) {
        return Order.builder()
                .status(dto.getStatus())
                .build();
    }

    public OrdersDto toDto(Order entity) {
        return OrdersDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .total(entity.getTotal())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public OrdersDto toDtoWithAllEntity(Order entity) {
        return OrdersDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .total(entity.getTotal())
                .status(entity.getStatus())
                .orderItemDtoList(orderItemMapper.toDtoList(entity.getOrderItems()))
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public Order updateEntity(OrdersDto dto, Order entity) {
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
//        if (dto.getOrderItemId() != null) {
//            entity.setOrderItems(List.of(orderItemRepository.findByIdAndDeletedAtIsNull(dto.getOrderItemId()).get()));
//        }
        return entity;
    }

    public List<OrdersDto> toDtoList(List<Order> entityList) {
        List<OrdersDto> dtoList = new ArrayList<>();
        for (Order entity : entityList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

}
