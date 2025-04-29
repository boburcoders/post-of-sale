package com.company.Pos_System.service.impl;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.OrdersDto;
import com.company.Pos_System.models.Order;
import com.company.Pos_System.models.Users;
import com.company.Pos_System.repository.OrderRepository;
import com.company.Pos_System.repository.UserRepository;
import com.company.Pos_System.service.mapper.OrderMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderMapper orderMapper;

    @Mock
    UserRepository userRepository;

    OrderServiceImpl orderService;
    OrdersDto ordersDto;
    Order order;
    Users user;

    @BeforeEach
    void setUp() {
        user = new Users();
        ordersDto = new OrdersDto();
        order = new Order();
        orderService = new OrderServiceImpl(orderRepository, userRepository, orderMapper);
    }

    @Test
    void createOrderForSuccess() {
        ordersDto.setUserId(1L);
        when(userRepository.findByIdAndDeletedAtIsNull(anyLong())).thenReturn(Optional.of(user));
        when(orderMapper.toEntity(ordersDto)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(ordersDto);

        HttpApiResponse<OrdersDto> response = orderService.createOrder(ordersDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("Order created successfully", response.getMessage());
        assertEquals(ordersDto, response.getData());

        verify(orderRepository, times(1)).save(order);
        verify(orderMapper, times(1)).toEntity(ordersDto);
        verify(orderMapper, times(1)).toDto(order);

    }

    @Test
    void createOrderForFailure() {
        ordersDto.setUserId(1L);
        when(userRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new EntityNotFoundException("User not found"));

        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(ordersDto));

        verifyNoInteractions(orderRepository, orderMapper);

    }

    @Test
    void getOrderByIdForSuccess() {
        when(orderRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDtoWithAllEntity(order)).thenReturn(ordersDto);

        HttpApiResponse<OrdersDto> response = orderService.getOrderById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("OK", response.getMessage());
        assertEquals(ordersDto, response.getData());

        verify(orderRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(orderMapper, times(1)).toDtoWithAllEntity(order);
    }


    @Test
    void getOrderByIdForFailure() {
        when(orderRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new EntityNotFoundException("Order not found"));

        assertThrows(EntityNotFoundException.class, () -> orderService.getOrderById(1L));

        verifyNoInteractions(orderMapper);
    }

    @Test
    void getAllOrdersForSuccess() {
        when(orderRepository.findAllByDeletedAtIsNull()).thenReturn(Optional.of(Collections.singletonList(order)));
        when(orderMapper.toDtoList(List.of(order))).thenReturn(List.of(ordersDto));

        HttpApiResponse<List<OrdersDto>> response = orderService.getAllOrders();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("OK", response.getMessage());
        assertEquals(List.of(ordersDto), response.getData());

        verify(orderRepository, times(1)).findAllByDeletedAtIsNull();
        verify(orderMapper, times(1)).toDtoList(List.of(order));

    }

    @Test
    void getAllOrdersForFailure() {
        when(orderRepository.findAllByDeletedAtIsNull()).thenThrow(new EntityNotFoundException("Orders not found"));

        assertThrows(EntityNotFoundException.class, () -> orderService.getAllOrders());

        verifyNoInteractions(orderMapper);
    }

    @Test
    void updateOrderByIdForSuccess() {
        when(orderRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(ordersDto);

        HttpApiResponse<OrdersDto> response = orderService.updateOrderById(1L, ordersDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Order updated successfully", response.getMessage());
        assertEquals(ordersDto, response.getData());

        verify(orderRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(orderMapper, times(1)).toDto(order);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void updateOrderByIdForFailure() {
        when(orderRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new EntityNotFoundException("Order not found"));

        assertThrows(EntityNotFoundException.class, () -> orderService.updateOrderById(1L, ordersDto));

        verifyNoInteractions(orderMapper);
    }

    @Test
    void deleteOrderByIdForSuccess() {
        when(orderRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        HttpApiResponse<String> response = orderService.deleteOrderById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Order deleted successfully", response.getMessage());

        verify(orderRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void deleteOrderByIdForFailure() {
        when(orderRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new EntityNotFoundException("Order not found"));
        assertThrows(EntityNotFoundException.class, () -> orderService.deleteOrderById(1L));
        verifyNoInteractions(orderMapper);
    }
}