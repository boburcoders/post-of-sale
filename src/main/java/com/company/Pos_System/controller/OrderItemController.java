package com.company.Pos_System.controller;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.OrderItemDto;
import com.company.Pos_System.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orderItems")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @PostMapping
    @Operation(summary = "Create OrderItem", description = "Create OrderItem With productId")
    HttpApiResponse<List<OrderItemDto>> createOrderItem(@RequestBody List<OrderItemDto> dtoList) {
        return orderItemService.createOrderItem(dtoList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get OrderItem", description = "Get OrderItem by Id")
    HttpApiResponse<OrderItemDto> getOrderItemById(@PathVariable Long id) {
        return orderItemService.getOrderItemById(id);
    }

    @GetMapping("/get-all")
    @Operation(summary = "Get All OrderItem", description = "Get All OrderItem ")
    public HttpApiResponse<List<OrderItemDto>> getAllOrderItems() {
        return orderItemService.getAllOrderItems();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update OrderItem", description = "Update OrderItem with id")
    HttpApiResponse<OrderItemDto> updateOrderById(@PathVariable Long id, @RequestBody OrderItemDto dto) {
        return orderItemService.updateOrderById(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete OrderItem", description = "Delete OrderItem with id")
    HttpApiResponse<String> deleteOrderById(@PathVariable Long id) {
        return orderItemService.deleteOrderById(id);
    }
}
