package com.company.Pos_System.repository;

import com.company.Pos_System.models.Category;
import com.company.Pos_System.models.Product;
import com.company.Pos_System.models.ProductInventory;
import com.company.Pos_System.models.WareHouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductInventoryRepositoryTest {
    @Autowired
    ProductInventoryRepository productInventoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    WareHouseRepository wareHouseRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Product product;
    WareHouse wareHouse;
    ProductInventory productInventory;


    @BeforeEach
    void setUp() {
        Category category = Category.builder()
                .description("description")
                .name("categoryName")
                .build();
        categoryRepository.save(category);

        wareHouse = WareHouse.builder()
                .name("Warehouse Name")
                .location("Location")
                .build();
        wareHouseRepository.save(wareHouse);

        product = Product.builder()
                .wareHouse(wareHouse)
                .name("productName")
                .price(BigDecimal.valueOf(1122))
                .serial("serial")
                .description("description")
                .category(category)
                .build();
        productRepository.save(product);

        productInventory = ProductInventory.builder()
                .warehouse(wareHouse)
                .product(product)
                .quantity(122)
                .build();
        productInventoryRepository.save(productInventory);
    }

    @Test
    void findByIdAndDeletedAtIsNull() {
        Optional<ProductInventory> result = productInventoryRepository.findByIdAndDeletedAtIsNull(productInventory.getId());
        assertTrue(result.isPresent());
        assertEquals(productInventory.getId(), result.get().getId());
    }

    @Test
    void findAllByDeletedAtIsNull() {
        Optional<List<ProductInventory>> resultList = productInventoryRepository.findAllByDeletedAtIsNull();
        assertTrue(resultList.isPresent());
        assertEquals(productInventory, resultList.get().get(0));
    }

    @Test
    void findByProductAndWarehouse() {
        Optional<ProductInventory> result = productInventoryRepository.findByProductAndWarehouse(product, wareHouse);
        assertTrue(result.isPresent());
        assertEquals(productInventory.getId(), result.get().getId());
    }
}