package com.company.Pos_System.service.impl;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.OrderItemDto;
import com.company.Pos_System.models.Order;
import com.company.Pos_System.models.OrderItem;
import com.company.Pos_System.models.Product;
import com.company.Pos_System.repository.OrderItemRepository;
import com.company.Pos_System.repository.OrderRepository;
import com.company.Pos_System.repository.ProductRepository;
import com.company.Pos_System.service.OrderItemService;
import com.company.Pos_System.service.mapper.OrderItemMapper;
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
import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public HttpApiResponse<List<OrderItemDto>> createOrderItem(List<OrderItemDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return HttpApiResponse.<List<OrderItemDto>>builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("OrderItem list cannot be null or empty")
                    .build();
        }

        List<OrderItem> orderItemsToSave = new ArrayList<>();

        for (OrderItemDto dto : dtoList) {
            Objects.requireNonNull(dto, "OrderItem DTO cannot be null");
            Objects.requireNonNull(dto.getProductId(), "Product ID cannot be null");
            Objects.requireNonNull(dto.getOrderId(), "Order ID cannot be null");

            Product product = productRepository.findByIdAndDeletedAtIsNull(dto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + dto.getProductId()));
            Order order = orderRepository.findByIdAndDeletedAtIsNull(dto.getOrderId())
                    .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + dto.getOrderId()));

            BigDecimal quantity = (dto.getQuantity() != null && dto.getQuantity().compareTo(BigDecimal.ZERO) > 0)
                    ? dto.getQuantity()
                    : BigDecimal.ONE;

            BigDecimal totalPrice = product.getPrice().multiply(quantity);

            BigDecimal orderTotalPrice = (order.getTotal() != null)
                    ? order.getTotal().add(totalPrice)
                    : totalPrice;
            order.setTotal(orderTotalPrice);

            OrderItem entity = orderItemMapper.toEntity(dto);
            entity.setOrder(order);
            entity.setProduct(product);
            entity.setQuantity(quantity);
            entity.setPrice(totalPrice);

            orderItemsToSave.add(entity);


            List<OrderItem> existingItems = order.getOrderItems() != null ? order.getOrderItems() : new ArrayList<>();
            existingItems.add(entity);
            order.setOrderItems(existingItems);
        }

        List<OrderItem> savedOrderItems = orderItemRepository.saveAll(orderItemsToSave);

        Set<Order> ordersToSave = orderItemsToSave.stream()
                .map(OrderItem::getOrder)
                .collect(Collectors.toSet());
        orderRepository.saveAll(ordersToSave);

        return HttpApiResponse.<List<OrderItemDto>>builder()
                .status(HttpStatus.CREATED)
                .message("OrderItems created successfully")
                .data(orderItemMapper.toDtoList(savedOrderItems))
                .build();
    }


    @Override
    public HttpApiResponse<OrderItemDto> getOrderItemById(Long id) {
        OrderItem orderItem = orderItemRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new EntityNotFoundException("OrderItem not found"));

        return HttpApiResponse.<OrderItemDto>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(orderItemMapper.toDto(orderItem))
                .build();
    }

    @Override
    public HttpApiResponse<List<OrderItemDto>> getAllOrderItems() {
        List<OrderItem> orderItemsList = orderItemRepository.findAllByDeletedAtIsNull().orElseThrow(
                () -> new EntityNotFoundException("OrderItem List not found"));

        return HttpApiResponse.<List<OrderItemDto>>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(orderItemMapper.toDtoList(orderItemsList))
                .build();
    }

    @Override
    public HttpApiResponse<OrderItemDto> updateOrderById(Long id, OrderItemDto dto) {

        OrderItem orderItem = orderItemRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new EntityNotFoundException("OrderItem not found"));

        OrderItem updateEntity = orderItemMapper.updateEntity(orderItem, dto);

        orderItemRepository.save(updateEntity);

        return HttpApiResponse.<OrderItemDto>builder()
                .status(HttpStatus.OK)
                .message("OrderItems updated successfully")
                .data(orderItemMapper.toDto(updateEntity))
                .build();
    }

    @Override
    public HttpApiResponse<String> deleteOrderById(Long id) {

        OrderItem orderItem = orderItemRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new EntityNotFoundException("OrderItem not found"));

        orderItem.setDeletedAt(LocalDateTime.now());

        orderItemRepository.save(orderItem);

        return HttpApiResponse.<String>builder()
                .status(HttpStatus.OK)
                .message("OrderItem deleted successfully")
                .build();
    }

}
