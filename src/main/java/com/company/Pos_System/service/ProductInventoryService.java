package com.company.Pos_System.service;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.ProductInventoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductInventoryService {
    HttpApiResponse<ProductInventoryDto> createProductInventory(ProductInventoryDto dto);

    HttpApiResponse<ProductInventoryDto> getProductInventoryById(Long id);

    HttpApiResponse<List<ProductInventoryDto>> getAllProductInventory();

    HttpApiResponse<ProductInventoryDto> updateProductInventoryById(Long id, ProductInventoryDto dto);

    HttpApiResponse<String> deleteProductInventoryById(Long id);
}
