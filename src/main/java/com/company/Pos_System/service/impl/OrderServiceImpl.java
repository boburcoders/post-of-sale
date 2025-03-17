package com.company.Pos_System.service.impl;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.OrdersDto;
import com.company.Pos_System.enums.OrderStatus;
import com.company.Pos_System.models.OrderItems;
import com.company.Pos_System.models.Orders;
import com.company.Pos_System.models.Users;
import com.company.Pos_System.repository.OrderItemRepository;
import com.company.Pos_System.repository.OrderRepository;
import com.company.Pos_System.repository.UserRepository;
import com.company.Pos_System.service.OrderService;
import com.company.Pos_System.service.mapper.OrderMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public HttpApiResponse<OrdersDto> createOrder(OrdersDto dto, List<Long> orderItemIds) {
        Objects.requireNonNull(dto, "Order DTO cannot be null");
        Objects.requireNonNull(orderItemIds, "Order Item IDs cannot be null or empty");

        if (orderItemIds.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one order item");
        }

        Users user = userRepository.findByIdAndDeletedAtIsNull(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + dto.getUserId()));

        List<OrderItems> orderItems = new ArrayList<>();
        for (Long orderItemId : orderItemIds) {
            orderItems.add(orderItemRepository.findByIdAndDeletedAtIsNull(orderItemId).orElseThrow(
                    () -> new EntityNotFoundException("Order Item not found with ID: " + orderItemId)));
        }
        if (orderItems.isEmpty()) {
            throw new EntityNotFoundException("No valid order items found for the given IDs");
        }

        Orders entity = orderMapper.toEntity(dto);
        entity.setUser(user);
        entity.setOrderItems(orderItems);
        entity.setStatus(OrderStatus.PENDING);
        entity.setTotal(entity.calculateTotal());

        // Update relationships
        orderItems.forEach(item -> item.setOrder(entity));

        Orders savedEntity = orderRepository.save(entity);

        return HttpApiResponse.<OrdersDto>builder()
                .status(HttpStatus.CREATED)
                .message("Order created successfully")
                .data(orderMapper.toDto(savedEntity))
                .build();
    }

    @Override
    public HttpApiResponse<OrdersDto> getOrderById(Long id) {
        Orders order = orderRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new EntityNotFoundException("Order Not Found"));

        return HttpApiResponse.<OrdersDto>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(orderMapper.toDto(order))
                .build();
    }

    @Override
    public HttpApiResponse<List<OrdersDto>> getAllOrders() {
        List<Orders> ordersList = orderRepository.findAllByDeletedAtIsNull().orElseThrow(
                () -> new EntityNotFoundException("Orders Not Found"));

        return HttpApiResponse.<List<OrdersDto>>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(orderMapper.toDtoList(ordersList))
                .build();
    }

    @Override
    public HttpApiResponse<OrdersDto> updateOrderById(Long id, OrdersDto dto) {
        Orders order = orderRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new EntityNotFoundException("Order Not Found"));
        Orders updatedEntity = orderMapper.updateEntity(dto, order);
        orderRepository.save(updatedEntity);

        return HttpApiResponse.<OrdersDto>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(orderMapper.toDto(updatedEntity))
                .build();
    }

    @Override
    public HttpApiResponse<String> deleteOrderById(Long id) {

        Orders order = orderRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new EntityNotFoundException("Order Not Found"));

        order.setDeletedAt(LocalDateTime.now());

        orderRepository.save(order);

        return HttpApiResponse.<String>builder()
                .status(HttpStatus.OK)
                .message("Order Deleted")
                .build();
    }
}
