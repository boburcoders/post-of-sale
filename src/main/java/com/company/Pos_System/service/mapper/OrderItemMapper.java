package com.company.Pos_System.service.mapper;

import com.company.Pos_System.dto.OrderItemDto;
import com.company.Pos_System.models.OrderItem;
import com.company.Pos_System.repository.OrderItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {
    private final ProductMapper productMapper;
    private final OrderItemRepository orderItemRepository;

    public OrderItem toEntity(OrderItemDto dto) {
        return OrderItem.builder()
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .build();
    }

    public OrderItemDto toDto(OrderItem entity) {
        return OrderItemDto.builder()
                .id(entity.getId())
                .orderId(entity.getOrder().getId())
                .price(entity.getPrice())
                .productId(entity.getProduct().getId())
                .quantity(entity.getQuantity())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public OrderItemDto toDtoWithAllEntity(OrderItem entity) {
        return OrderItemDto.builder()
                .id(entity.getId())
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public OrderItem updateEntity(OrderItem entity, OrderItemDto dto) {
        if (dto.getQuantity() != null) {
            entity.setQuantity(dto.getQuantity());
        }
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }
        return entity;
    }

    public List<OrderItemDto> toDtoList(List<OrderItem> entityList) {
        List<OrderItemDto> dtoList = new ArrayList<>();
        for (OrderItem entity : entityList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

    public List<Long> toDtoListWithID(List<OrderItem> entityList) {
        List<Long> dtoList = new ArrayList<>();
        for (OrderItem entity : entityList) {
            dtoList.add(entity.getId());
        }
        return dtoList;
    }

    public List<OrderItem> toEntityList(List<OrderItemDto> dtoList) {
        List<OrderItem> entityList = new ArrayList<>();
        for (OrderItemDto dto : dtoList) {
            entityList.add(toEntity(dto));
        }
        return entityList;
    }

    public List<OrderItem> toEntityListFromId(List<Long> idList) {
        List<OrderItem> entityList = new ArrayList<>();
        for (Long id : idList) {
            entityList.add(orderItemRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                    () -> new EntityNotFoundException("OrderItem with ID: " + id)));
        }
        return entityList;
    }
}
