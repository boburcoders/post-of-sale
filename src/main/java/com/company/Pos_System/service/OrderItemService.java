package com.company.Pos_System.service;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.OrderItemDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderItemService {
    HttpApiResponse<OrderItemDto> createOrderItem(Long productId, OrderItemDto dto);

    HttpApiResponse<OrderItemDto> getOrderItemById(Long id);

    HttpApiResponse<OrderItemDto> updateOrderById(Long id, OrderItemDto dto);

    HttpApiResponse<String> deleteOrderById(Long id);

    HttpApiResponse<List<OrderItemDto>> getAllOrderItems();
}
