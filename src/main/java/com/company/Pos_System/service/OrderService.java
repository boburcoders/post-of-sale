package com.company.Pos_System.service;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.OrdersDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    HttpApiResponse<OrdersDto> createOrder(OrdersDto dto, List<Long> orderItemIds);

    HttpApiResponse<OrdersDto> getOrderById(Long id);

    HttpApiResponse<OrdersDto> updateOrderById(Long id, OrdersDto dto);

    HttpApiResponse<String> deleteOrderById(Long id);

    HttpApiResponse<List<OrdersDto>> getAllOrders();
}
