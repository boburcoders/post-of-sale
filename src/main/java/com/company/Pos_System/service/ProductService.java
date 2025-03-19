package com.company.Pos_System.service;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface ProductService {
    HttpApiResponse<ProductDto> createProduct(ProductDto dto);

    HttpApiResponse<ProductDto> getProductById(Long id);

    HttpApiResponse<ProductDto> updateProductById(Long id,ProductDto dto);

    HttpApiResponse<String> deleteProductById(Long id);

    HttpApiResponse<Set<ProductDto>> getAllProducts();

    HttpApiResponse<Set<ProductDto>> getAllProductsByCategory(String categoryName);
}
