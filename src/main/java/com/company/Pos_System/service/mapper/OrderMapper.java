package com.company.Pos_System.service.mapper;

import com.company.Pos_System.dto.OrdersDto;
import com.company.Pos_System.models.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;

    public Orders toEntity(OrdersDto dto) {
        return Orders.builder()
                .build();
    }

    public OrdersDto toDto(Orders entity) {
        return OrdersDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .total(entity.getTotal())
                .orderItems(orderItemMapper.toDtoListWithID(entity.getOrderItems()))
                .build();
    }

    public Orders updateEntity(OrdersDto dto, Orders entity) {
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getOrderItems() != null) {
            entity.setOrderItems(orderItemMapper.toEntityListFromId(dto.getOrderItems()));
        }
        return entity;
    }

    public List<OrdersDto> toDtoList(List<Orders> entityList) {
        List<OrdersDto> dtoList = new ArrayList<>();
        for (Orders entity : entityList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

}
