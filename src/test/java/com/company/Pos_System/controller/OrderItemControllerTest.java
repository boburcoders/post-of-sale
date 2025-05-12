package com.company.Pos_System.controller;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.OrderItemDto;
import com.company.Pos_System.models.enums.OrderStatus;
import com.company.Pos_System.models.Order;
import com.company.Pos_System.models.OrderItem;
import com.company.Pos_System.models.Product;
import com.company.Pos_System.models.Users;
import com.company.Pos_System.service.impl.OrderItemServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class OrderItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Mock
    Order order;

    @MockBean
    OrderItemServiceImpl orderItemService;

    OrderItem orderItem;
    OrderItemDto orderItemDto;

    private static final String USERNAME = "BOBUR_ADMIN";
    private static final String PASSWORD = "Admin@123";
    private static final String ROLE = "ADMIN";

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .user(new Users())
                .status(OrderStatus.PENDING)
                .build();
        orderItem = OrderItem.builder()
                .order(order)
                .product(new Product())
                .price(BigDecimal.valueOf(12))
                .quantity(BigDecimal.valueOf(1))
                .build();

        orderItemDto = OrderItemDto.builder()
                .orderId(1L)
                .productId(1L)
                .quantity(BigDecimal.valueOf(1))
                .price(BigDecimal.valueOf(12))
                .build();

        HttpApiResponse<OrderItemDto> response = HttpApiResponse.<OrderItemDto>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(orderItemDto)
                .build();

        HttpApiResponse<List<OrderItemDto>> listHttpApiResponse = HttpApiResponse.<List<OrderItemDto>>builder()
                .data(List.of(orderItemDto))
                .status(HttpStatus.OK)
                .message("OK")
                .build();

        when(orderItemService.createOrderItem(List.of(orderItemDto))).thenReturn(listHttpApiResponse);
        when(orderItemService.getOrderItemById(1L)).thenReturn(response);
        when(orderItemService.updateOrderById(1L, orderItemDto)).thenReturn(response);
        when(orderItemService.deleteOrderById(1L)).thenReturn(HttpApiResponse.<String>builder()
                .message("OrderItem deleted successfully")
                .status(HttpStatus.OK)
                .build());
    }

    private String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void createOrderItem() throws Exception {
        mockMvc.perform(post("/api/orderItems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(orderItemDto)))
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getOrderItemById() throws Exception {
        mockMvc.perform(get("/api/orderItems/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllOrderItems() throws Exception {
        mockMvc.perform(get("/api/orderItems/get-all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateOrderById() throws Exception {
        mockMvc.perform(put("/api/orderItems/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(orderItemDto))
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteOrderById() throws Exception {
        mockMvc.perform(delete("/api/orderItems/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles(ROLE)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}