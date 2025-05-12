package com.company.Pos_System.service.impl;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.OrderItemDto;
import com.company.Pos_System.dto.ProductInventoryDto;
import com.company.Pos_System.models.Order;
import com.company.Pos_System.models.OrderItem;
import com.company.Pos_System.models.Product;
import com.company.Pos_System.models.ProductInventory;
import com.company.Pos_System.repository.OrderItemRepository;
import com.company.Pos_System.repository.OrderRepository;
import com.company.Pos_System.repository.ProductInventoryRepository;
import com.company.Pos_System.repository.ProductRepository;
import com.company.Pos_System.service.mapper.OrderItemMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplTest {
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemMapper orderItemMapper;

    @Mock
    ProductRepository productRepository;
    @Mock
    ProductInventoryRepository productInventoryRepository;

    OrderItemServiceImpl orderItemService;

    OrderItem orderItem;
    OrderItemDto orderItemDto;
    Product product;
    Order order;
    ProductInventory productInventory;
    ProductInventoryDto productInventoryDto;


    @BeforeEach
    void setUp() {
        productInventory = new ProductInventory();
        productInventoryDto = new ProductInventoryDto();
        order = new Order();
        product = new Product();
        orderItem = new OrderItem();
        orderItemDto = new OrderItemDto();
        orderItemService = new OrderItemServiceImpl(orderItemRepository, productRepository,
                orderRepository, orderItemMapper, productInventoryRepository);
    }

    @Test
    void createOrderItemForSuccess() {
        product.setId(1L);
        product.setPrice(BigDecimal.TEN);
        order.setId(1L);
        productInventory.setProduct(product);
        productInventory.setWarehouse(order.getWarehouse());
        productInventory.setQuantity(10);
        orderItemDto.setProductId(product.getId());
        orderItemDto.setOrderId(order.getId());

        when(productRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(product));
        when(orderRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(order));
        when(orderItemMapper.toEntity(orderItemDto)).thenReturn(orderItem);
        when(orderItemRepository.saveAll(List.of(orderItem))).thenReturn(List.of(orderItem));
        when(orderItemMapper.toDtoList(List.of(orderItem))).thenReturn(List.of(orderItemDto));
        when(productInventoryRepository.findByProductAndWarehouse(product, order.getWarehouse())).thenReturn(Optional.of(productInventory));

        HttpApiResponse<List<OrderItemDto>> response = orderItemService.createOrderItem(List.of(orderItemDto));

        assertNotNull(response);
        assertEquals("OrderItems created successfully", response.getMessage());
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals(List.of(orderItemDto), response.getData());

        verify(productRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(orderRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(orderItemMapper, times(1)).toEntity(orderItemDto);
        verify(orderItemMapper, times(1)).toDtoList(List.of(orderItem));
    }

    @Test
    void createOrderItemForFailure() {
        product.setId(1L);
        product.setPrice(BigDecimal.TEN);
        order.setId(1L);
        orderItemDto.setProductId(product.getId());
        orderItemDto.setOrderId(order.getId());

        when(productRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new EntityNotFoundException("Product not found"));
        assertThrows(EntityNotFoundException.class, () -> orderItemService.createOrderItem(List.of(orderItemDto)));

        verify(productRepository).findByIdAndDeletedAtIsNull(1L);
        verifyNoInteractions(orderItemMapper);
    }

    @Test
    void getOrderItemByIdForSuccess() {
        when(orderItemRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(orderItem));
        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemDto);

        HttpApiResponse<OrderItemDto> response = orderItemService.getOrderItemById(1L);

        assertNotNull(response);
        assertEquals("OK", response.getMessage());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(orderItemDto, response.getData());

        verify(orderItemRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(orderItemMapper, times(1)).toDto(orderItem);
    }


    @Test
    void getOrderItemByIdForFailure() {
        when(orderItemRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new EntityNotFoundException("OrderItem not found"));
        assertThrows(EntityNotFoundException.class, () -> orderItemService.getOrderItemById(1L));
        verifyNoInteractions(orderItemMapper);
    }

    @Test
    void getAllOrderItemsForSuccess() {
        when(orderItemRepository.findAllByDeletedAtIsNull()).thenReturn(Optional.of(List.of(orderItem)));
        when(orderItemMapper.toDtoList(List.of(orderItem))).thenReturn(List.of(orderItemDto));

        HttpApiResponse<List<OrderItemDto>> response = orderItemService.getAllOrderItems();

        assertNotNull(response);
        assertEquals("OK", response.getMessage());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(List.of(orderItemDto), response.getData());

        verify(orderItemRepository, times(1)).findAllByDeletedAtIsNull();
        verify(orderItemMapper, times(1)).toDtoList(List.of(orderItem));
    }

    @Test
    void getAllOrderItemsForFailure() {
        when(orderItemRepository.findAllByDeletedAtIsNull()).thenThrow(new EntityNotFoundException("OrderItems not found"));
        assertThrows(EntityNotFoundException.class, () -> orderItemService.getAllOrderItems());
        verify(orderItemRepository, times(1)).findAllByDeletedAtIsNull();
        verifyNoInteractions(orderItemMapper);
    }

    @Test
    void updateOrderByIdForSuccess() {
        when(orderItemRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(orderItem));
        when(orderItemMapper.updateEntity(orderItem, orderItemDto)).thenReturn(orderItem);
        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemDto);

        HttpApiResponse<OrderItemDto> response = orderItemService.updateOrderById(1L, orderItemDto);

        assertNotNull(response);
        assertEquals("OrderItems updated successfully", response.getMessage());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(orderItemDto, response.getData());

        verify(orderItemRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(orderItemMapper, times(1)).updateEntity(orderItem, orderItemDto);
        verify(orderItemMapper, times(1)).toDto(orderItem);
    }

    @Test
    void updateOrderByIdForFailure() {
        when(orderItemRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new EntityNotFoundException("OrderItem not found"));

        assertThrows(EntityNotFoundException.class, () -> orderItemService.updateOrderById(1L, orderItemDto));

        verify(orderItemRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verifyNoInteractions(orderItemMapper);
    }

    @Test
    void deleteOrderByIdForSuccess() {
        when(orderItemRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(orderItem));

        HttpApiResponse<String> response = orderItemService.deleteOrderById(1L);

        assertNotNull(response);
        assertEquals("OrderItem deleted successfully", response.getMessage());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertNull(response.getData());

        verify(orderItemRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(orderItemRepository, times(1)).save(orderItem);
    }


    @Test
    void deleteOrderByIdForFailure() {
        when(orderItemRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new EntityNotFoundException("OrderItem not found"));
        assertThrows(EntityNotFoundException.class, () -> orderItemService.deleteOrderById(1L));
        verify(orderItemRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verifyNoInteractions(orderItemMapper);
    }
}