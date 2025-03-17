package com.company.Pos_System.service.mapper;

import com.company.Pos_System.dto.OrderItemDto;
import com.company.Pos_System.models.OrderItems;
import com.company.Pos_System.repository.OrderItemRepository;
import com.company.Pos_System.repository.ProductRepository;
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

    public OrderItems toEntity(OrderItemDto dto) {
        return OrderItems.builder()
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .build();
    }

    public OrderItemDto toDto(OrderItems entity) {
        return OrderItemDto.builder()
                .id(entity.getId())
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .build();
    }

    public OrderItems updateEntity(OrderItems entity, OrderItemDto dto) {
        if (dto.getQuantity() != null) {
            entity.setQuantity(dto.getQuantity());
        }
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }
        return entity;
    }

    public List<OrderItemDto> toDtoList(List<OrderItems> entityList) {
        List<OrderItemDto> dtoList = new ArrayList<>();
        for (OrderItems entity : entityList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

    public List<Long> toDtoListWithID(List<OrderItems> entityList) {
        List<Long> dtoList = new ArrayList<>();
        for (OrderItems entity : entityList) {
            dtoList.add(entity.getId());
        }
        return dtoList;
    }

    public List<OrderItems> toEntityList(List<OrderItemDto> dtoList) {
        List<OrderItems> entityList = new ArrayList<>();
        for (OrderItemDto dto : dtoList) {
            entityList.add(toEntity(dto));
        }
        return entityList;
    }

    public List<OrderItems> toEntityListFromId(List<Long> idList) {
        List<OrderItems> entityList = new ArrayList<>();
        for (Long id : idList) {
            entityList.add(orderItemRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                    () -> new EntityNotFoundException("OrderItem with ID: " + id)));
        }
        return entityList;
    }
}
