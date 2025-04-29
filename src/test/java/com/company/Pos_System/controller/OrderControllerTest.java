package com.company.Pos_System.controller;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.OrdersDto;
import com.company.Pos_System.enums.OrderStatus;
import com.company.Pos_System.enums.UserRole;
import com.company.Pos_System.models.Order;
import com.company.Pos_System.models.Users;
import com.company.Pos_System.service.impl.OrderServiceImpl;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    OrderServiceImpl orderService;

    @Mock
    Users user;

    Order order;
    OrdersDto ordersDto;

    private static final String USERNAME = "BOBUR_ADMIN";
    private static final String PASSWORD = "Admin@123";

    @BeforeEach
    void setUp() {
        user = Users.builder()
                .fullName("Full Name")
                .username("BOBUR_ADMIN")
                .password("<PASSWORD>")
                .role(UserRole.ADMIN)
                .build();
        order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .build();
        ordersDto = OrdersDto.builder()
                .id(1L)
                .userId(1L)
                .status(OrderStatus.PENDING)
                .build();
        HttpApiResponse<OrdersDto> response = HttpApiResponse.<OrdersDto>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(ordersDto)
                .build();

        HttpApiResponse<List<OrdersDto>> getAllOrdersResponse = HttpApiResponse.<List<OrdersDto>>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(List.of(ordersDto))
                .build();

        when(orderService.createOrder(ordersDto)).thenReturn(response);
        when(orderService.getOrderById(1L)).thenReturn(response);
        when(orderService.updateOrderById(1L, ordersDto)).thenReturn(response);
        when(orderService.deleteOrderById(1L)).thenReturn(
                HttpApiResponse.<String>builder()
                        .status(HttpStatus.OK)
                        .message("Order deleted successfully")
                        .build()
        );
        when(orderService.getAllOrders()).thenReturn(getAllOrdersResponse);
    }

    private String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void createOrder() throws Exception {
        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(ordersDto))
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getOrderById() throws Exception {
        mockMvc.perform(get("/api/order/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllOrders() throws Exception {
        mockMvc.perform(get("/api/order/get-all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateOrderById() throws Exception {
        mockMvc.perform(put("/api/order/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(ordersDto))
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteOrderById() throws Exception {
        mockMvc.perform(delete("/api/order/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(USERNAME).password(PASSWORD).roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}