package com.company.Pos_System.service.impl;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.ProductInventoryDto;
import com.company.Pos_System.exceptions.ItemNotFoundException;
import com.company.Pos_System.models.Product;
import com.company.Pos_System.models.ProductInventory;
import com.company.Pos_System.models.WareHouse;
import com.company.Pos_System.repository.ProductInventoryRepository;
import com.company.Pos_System.repository.ProductRepository;
import com.company.Pos_System.repository.WareHouseRepository;
import com.company.Pos_System.service.ProductInventoryService;
import com.company.Pos_System.service.mapper.ProductInventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductInventoryServiceImpl implements ProductInventoryService {

    private final ProductInventoryRepository productInventoryRepository;
    private final WareHouseRepository wareHouseRepository;
    private final ProductRepository productRepository;
    private final ProductInventoryMapper productInventoryMapper;

    @Override
    public HttpApiResponse<ProductInventoryDto> createProductInventory(ProductInventoryDto dto) {
        try {

            Product product = productRepository.findByIdAndDeletedAtIsNull(dto.getProductId())
                    .orElseThrow(() -> new ItemNotFoundException("Product not found with ID: " + dto.getProductId()));

            WareHouse wareHouse = wareHouseRepository.findByIdAndDeletedAtIsNull(dto.getWarehouseId())
                    .orElseThrow(() -> new ItemNotFoundException("Warehouse not found with ID: " + dto.getWarehouseId()));

            ProductInventory entity = productInventoryMapper.toEntity(dto);
            entity.setProduct(product);
            entity.setWarehouse(wareHouse);

            productInventoryRepository.saveAndFlush(entity);

            return HttpApiResponse.<ProductInventoryDto>builder()
                    .success(true)
                    .status(HttpStatus.CREATED)
                    .message("Product Inventory created successfully")
                    .data(productInventoryMapper.toDto(entity))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public HttpApiResponse<ProductInventoryDto> getProductInventoryById(Long id) {
        ProductInventory productInventory = productInventoryRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ItemNotFoundException("Product Inventory not found with ID: " + id));


        return HttpApiResponse.<ProductInventoryDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("OK")
                .data(productInventoryMapper.toDto(productInventory))
                .build();
    }

    @Override
    public HttpApiResponse<List<ProductInventoryDto>> getAllProductInventory() {
        List<ProductInventory> productInventoryList = productInventoryRepository.findAllByDeletedAtIsNull()
                .orElseThrow(() -> new ItemNotFoundException("Product Inventory List not found"));

        return HttpApiResponse.<List<ProductInventoryDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("OK")
                .data(productInventoryMapper.toDtoList(productInventoryList))
                .build();
    }

    @Override
    public HttpApiResponse<ProductInventoryDto> updateProductInventoryById(Long id, ProductInventoryDto dto) {

        ProductInventory productInventory = productInventoryRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ItemNotFoundException("Product Inventory not found with ID: " + id));

        ProductInventory updatedEntity = productInventoryMapper.updateEntity(productInventory, dto);

        productInventoryRepository.saveAndFlush(updatedEntity);

        return HttpApiResponse.<ProductInventoryDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Product Inventory updated successfully")
                .data(productInventoryMapper.toDto(updatedEntity))
                .build();
    }

    @Override
    public HttpApiResponse<String> deleteProductInventoryById(Long id) {

        ProductInventory productInventory = productInventoryRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ItemNotFoundException("Product Inventory not found with ID: " + id));

        productInventory.setDeletedAt(LocalDateTime.now());
        productInventoryRepository.saveAndFlush(productInventory);

        return HttpApiResponse.<String>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Product Inventory deleted successfully")
                .build();
    }
}
