package com.company.Pos_System.service.impl;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.OrderItemDto;
import com.company.Pos_System.models.OrderItems;
import com.company.Pos_System.models.Product;
import com.company.Pos_System.repository.OrderItemRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public HttpApiResponse<OrderItemDto> createOrderItem(Long productId, OrderItemDto dto) {
        Objects.requireNonNull(productId, "Product ID cannot be null");
        Objects.requireNonNull(dto, "OrderItem DTO cannot be null");

        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        // Ensure valid quantity
        BigDecimal quantity = (dto.getQuantity() != null && dto.getQuantity().compareTo(BigDecimal.ZERO) > 0)
                ? dto.getQuantity()
                : BigDecimal.ONE;

        BigDecimal price = product.getPrice().multiply(quantity); // Use product price

        OrderItems entity = orderItemMapper.toEntity(dto);
        entity.setProduct(product);
        entity.setQuantity(quantity);
        entity.setPrice(price);

        OrderItems savedEntity = orderItemRepository.save(entity);

        return HttpApiResponse.<OrderItemDto>builder()
                .status(HttpStatus.CREATED)
                .message("OrderItem created successfully")
                .data(orderItemMapper.toDto(savedEntity))
                .build();
    }

    @Override
    public HttpApiResponse<OrderItemDto> getOrderItemById(Long id) {
        OrderItems orderItem = orderItemRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new EntityNotFoundException("OrderItem not found"));

        return HttpApiResponse.<OrderItemDto>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(orderItemMapper.toDto(orderItem))
                .build();
    }

    @Override
    public HttpApiResponse<List<OrderItemDto>> getAllOrderItems() {
        List<OrderItems> orderItemsList = orderItemRepository.findAllByDeletedAtIsNull().orElseThrow(
                () -> new EntityNotFoundException("OrderItem List not found"));

        return HttpApiResponse.<List<OrderItemDto>>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(orderItemMapper.toDtoList(orderItemsList))
                .build();
    }

    @Override
    public HttpApiResponse<OrderItemDto> updateOrderById(Long id, OrderItemDto dto) {

        OrderItems orderItem = orderItemRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new EntityNotFoundException("OrderItem not found"));

        OrderItems updateEntity = orderItemMapper.updateEntity(orderItem, dto);

        orderItemRepository.save(updateEntity);

        return HttpApiResponse.<OrderItemDto>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(orderItemMapper.toDto(updateEntity))
                .build();
    }

    @Override
    public HttpApiResponse<String> deleteOrderById(Long id) {

        OrderItems orderItem = orderItemRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new EntityNotFoundException("OrderItem not found"));

        orderItem.setDeletedAt(LocalDateTime.now());

        orderItemRepository.save(orderItem);

        return HttpApiResponse.<String>builder()
                .status(HttpStatus.OK)
                .message("OrderItem deleted successfully")
                .build();
    }

    public BigDecimal validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        return price;
    }
}
