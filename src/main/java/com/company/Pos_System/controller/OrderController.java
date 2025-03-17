package com.company.Pos_System.controller;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.OrdersDto;
import com.company.Pos_System.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a Order", description = "Create a order")
    HttpApiResponse<OrdersDto> createOrder(@RequestBody OrdersDto dto) {
        return orderService.createOrder(dto, dto.getOrderItems());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Order", description = "Get a order by Id")
    HttpApiResponse<OrdersDto> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/get-all")
    @Operation(summary = "Get All Order", description = "Get All Orders")
    public HttpApiResponse<List<OrdersDto>> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Order", description = "Update a order by Id")
    HttpApiResponse<OrdersDto> updateOrderById(@PathVariable Long id, @RequestBody OrdersDto dto) {
        return orderService.updateOrderById(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Delete Order", description = "Delete a order by Id")
    HttpApiResponse<String> deleteOrderById(@PathVariable Long id) {
        return orderService.deleteOrderById(id);
    }
}
