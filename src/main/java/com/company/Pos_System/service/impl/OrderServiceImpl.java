package com.company.Pos_System.service.impl;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.OrdersDto;
import com.company.Pos_System.models.Order;
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
    public HttpApiResponse<OrdersDto> createOrder(OrdersDto dto) {
        Objects.requireNonNull(dto, "Order DTO cannot be null");

        Users user = userRepository.findByIdAndDeletedAtIsNull(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + dto.getUserId()));

        Order entity = orderMapper.toEntity(dto);
        entity.setUser(user);

        BigDecimal newPrice = entity.getTotal() != null ? entity.getTotal() : BigDecimal.ZERO; // Ensure no null value
        entity.setTotal(newPrice);


        Order savedEntity = orderRepository.save(entity);

        return HttpApiResponse.<OrdersDto>builder()
                .status(HttpStatus.CREATED)
                .message("Order created successfully")
                .data(orderMapper.toDto(savedEntity))
                .build();
    }


    @Override
    public HttpApiResponse<OrdersDto> getOrderById(Long id) {
        Order order = orderRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new EntityNotFoundException("Order Not Found"));

        return HttpApiResponse.<OrdersDto>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(orderMapper.toDtoWithAllEntity(order))
                .build();
    }

    @Override
    public HttpApiResponse<List<OrdersDto>> getAllOrders() {
        List<Order> ordersList = orderRepository.findAllByDeletedAtIsNull().orElseThrow(
                () -> new EntityNotFoundException("Orders Not Found"));

        return HttpApiResponse.<List<OrdersDto>>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(orderMapper.toDtoList(ordersList))
                .build();
    }

    @Override
    public HttpApiResponse<OrdersDto> updateOrderById(Long id, OrdersDto dto) {
        Order order = orderRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new EntityNotFoundException("Order Not Found"));

        orderMapper.updateEntity(dto, order);
        order = orderRepository.save(order);

        return HttpApiResponse.<OrdersDto>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(orderMapper.toDto(order))
                .build();
    }

    @Override
    public HttpApiResponse<String> deleteOrderById(Long id) {

        Order order = orderRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new EntityNotFoundException("Order Not Found"));

        order.setDeletedAt(LocalDateTime.now());

        orderRepository.save(order);

        return HttpApiResponse.<String>builder()
                .status(HttpStatus.OK)
                .message("Order Deleted")
                .build();
    }
}
