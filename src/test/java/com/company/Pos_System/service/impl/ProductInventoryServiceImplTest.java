package com.company.Pos_System.service.impl;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.ProductInventoryDto;
import com.company.Pos_System.exceptions.ItemNotFoundException;
import com.company.Pos_System.models.Category;
import com.company.Pos_System.models.Product;
import com.company.Pos_System.models.ProductInventory;
import com.company.Pos_System.models.WareHouse;
import com.company.Pos_System.repository.ProductInventoryRepository;
import com.company.Pos_System.repository.ProductRepository;
import com.company.Pos_System.repository.WareHouseRepository;
import com.company.Pos_System.service.mapper.ProductInventoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductInventoryServiceImplTest {

    @Mock
    private ProductInventoryRepository productInventoryRepository;
    @Mock
    private WareHouseRepository wareHouseRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductInventoryMapper productInventoryMapper;

    ProductInventoryServiceImpl productInventoryService;

    ProductInventory productInventory;
    ProductInventoryDto productInventoryDto;
    Product product;
    WareHouse wareHouse;
    Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        wareHouse = new WareHouse();
        wareHouse.setId(1L);
        product = new Product();
        product.setId(1L);
        product.setWareHouse(wareHouse);
        product.setCategory(category);

        productInventory = ProductInventory.builder()
                .product(product)
                .warehouse(wareHouse)
                .quantity(10)
                .build();

        productInventoryDto = ProductInventoryDto.builder()
                .id(1L)
                .productId(1L)
                .warehouseId(1L)
                .quantity(10)
                .build();

        productInventoryService = new ProductInventoryServiceImpl(productInventoryRepository, wareHouseRepository, productRepository, productInventoryMapper);
    }

    @Test
    void createProductInventoryForSuccessTest() {
        when(productRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(product));
        when(wareHouseRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(wareHouse));
        when(productInventoryMapper.toEntity(productInventoryDto)).thenReturn(productInventory);
        when(productInventoryRepository.saveAndFlush(productInventory)).thenReturn(productInventory);
        when(productInventoryMapper.toDto(productInventory)).thenReturn(productInventoryDto);

        HttpApiResponse<ProductInventoryDto> response = productInventoryService.createProductInventory(productInventoryDto);

        assertTrue(response.isSuccess());
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("Product Inventory created successfully", response.getMessage());
        assertEquals(productInventoryDto, response.getData());

        verify(productRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(wareHouseRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(productInventoryMapper, times(1)).toEntity(productInventoryDto);
        verify(productInventoryRepository, times(1)).saveAndFlush(productInventory);
    }

    @Test
    void createProductInventoryForProductNotFoundExceptionTest() {
        when(productRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new ItemNotFoundException("Product not found"));

        assertThrows(RuntimeException.class, () -> productInventoryService.createProductInventory(productInventoryDto));

        verifyNoInteractions(wareHouseRepository, productInventoryMapper, productInventoryRepository);
        verify(productRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
    }

    @Test
    void getProductInventoryByIdForSuccessTest() {
        when(productInventoryRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(productInventory));
        when(productInventoryMapper.toDto(productInventory)).thenReturn(productInventoryDto);

        HttpApiResponse<ProductInventoryDto> response = productInventoryService.getProductInventoryById(1L);

        assertTrue(response.isSuccess());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("OK", response.getMessage());
        assertEquals(productInventoryDto, response.getData());

        verify(productInventoryRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(productInventoryMapper, times(1)).toDto(productInventory);
        verifyNoInteractions(productRepository, wareHouseRepository);
    }

    @Test
    void getProductInventoryByIdForFailureTest() {
        when(productInventoryRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new ItemNotFoundException("Product Inventory not found"));
        assertThrows(ItemNotFoundException.class, () -> productInventoryService.getProductInventoryById(1L));
        verifyNoInteractions(productInventoryMapper, productRepository, wareHouseRepository);
    }

    @Test
    void getAllProductInventoryForSuccessTes() {
        when(productInventoryRepository.findAllByDeletedAtIsNull()).thenReturn(Optional.of(List.of(productInventory)));
        when(productInventoryMapper.toDtoList(List.of(productInventory))).thenReturn(List.of(productInventoryDto));

        HttpApiResponse<List<ProductInventoryDto>> response = productInventoryService.getAllProductInventory();

        assertTrue(response.isSuccess());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("OK", response.getMessage());
        assertEquals(List.of(productInventoryDto), response.getData());

        verify(productInventoryRepository, times(1)).findAllByDeletedAtIsNull();
        verify(productInventoryMapper, times(1)).toDtoList(List.of(productInventory));
        verifyNoInteractions(productRepository, wareHouseRepository);
    }

    @Test
    void getAllProductInventoryForFailureTes() {
        when(productInventoryRepository.findAllByDeletedAtIsNull()).thenThrow(new ItemNotFoundException("ProductInventories not found"));
        assertThrows(ItemNotFoundException.class, () -> productInventoryService.getAllProductInventory());
        verifyNoInteractions(productInventoryMapper, productRepository, wareHouseRepository);
    }

    @Test
    void updateProductInventoryByIdForSuccessTest() {
        when(productInventoryRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(productInventory));
        when(productInventoryMapper.updateEntity(productInventory, productInventoryDto)).thenReturn(productInventory);
        when(productInventoryRepository.saveAndFlush(productInventory)).thenReturn(productInventory);
        when(productInventoryMapper.toDto(productInventory)).thenReturn(productInventoryDto);

        HttpApiResponse<ProductInventoryDto> response = productInventoryService.updateProductInventoryById(1L, productInventoryDto);

        assertTrue(response.isSuccess());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Product Inventory updated successfully", response.getMessage());
        assertEquals(productInventoryDto, response.getData());

        verify(productInventoryRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(productInventoryMapper, times(1)).updateEntity(productInventory, productInventoryDto);
        verify(productInventoryRepository, times(1)).saveAndFlush(productInventory);
        verify(productInventoryMapper, times(1)).toDto(productInventory);
        verifyNoInteractions(productRepository, wareHouseRepository);
    }

    @Test
    void updateProductInventoryByIdForFailureTest() {
        when(productInventoryRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new ItemNotFoundException("Product Inventory not found"));
        assertThrows(ItemNotFoundException.class, () -> productInventoryService.updateProductInventoryById(1L, productInventoryDto));
        verifyNoInteractions(productInventoryMapper, productRepository, wareHouseRepository);
    }

    @Test
    void deleteProductInventoryByIdForSuccessTest() {
        when(productInventoryRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(productInventory));
        when(productInventoryRepository.saveAndFlush(productInventory)).thenReturn(productInventory);

        HttpApiResponse<String> response = productInventoryService.deleteProductInventoryById(1L);
        assertTrue(response.isSuccess());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Product Inventory deleted successfully", response.getMessage());

        verify(productInventoryRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(productInventoryRepository, times(1)).saveAndFlush(productInventory);
        verifyNoInteractions(productRepository, wareHouseRepository, productInventoryMapper);
    }


    @Test
    void deleteProductInventoryByIdForFailureTest() {
        when(productInventoryRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new ItemNotFoundException("Product Inventory not found"));
        assertThrows(ItemNotFoundException.class, () -> productInventoryService.deleteProductInventoryById(1L));
        verifyNoInteractions(productInventoryMapper, productRepository, wareHouseRepository);
    }
}