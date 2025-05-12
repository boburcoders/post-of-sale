package com.company.Pos_System.repository;

import com.company.Pos_System.models.Category;
import com.company.Pos_System.models.Product;
import com.company.Pos_System.models.WareHouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    WareHouseRepository wareHouseRepository;
    @Mock
    Category category;

    Product product;
    WareHouse wareHouse;

    @BeforeEach
    void setUp() {
        wareHouse = WareHouse.builder()
                .name("Warehouse Name")
                .location("Location")
                .build();
        wareHouseRepository.save(wareHouse);
        category = Category.builder()
                .name("categoryName")
                .description("description")
                .build();
        categoryRepository.save(category);
        product = Product.builder()
                .category(category)
                .name("productName")
                .serial("serial")
                .price(BigDecimal.valueOf(122))
                .wareHouse(wareHouse)
                .description("description")
                .build();
        productRepository.save(product);
    }

    @Test
    void findByIdAndDeletedAtIsNull() {
        Optional<Product> result = productRepository.findByIdAndDeletedAtIsNull(product.getId());
        assertTrue(result.isPresent());
        assertEquals(product.getId(), result.get().getId());
    }

    @Test
    void findAllByDeletedAtIsNull() {
        Optional<Set<Product>> resultSet = productRepository.findAllByDeletedAtIsNull();
        assertTrue(resultSet.isPresent());
        assertEquals(product, resultSet.get().stream().findFirst().get());
    }

    @Test
    void findAllByCategoryName() {
        Optional<Set<Product>> allByCategoryName = productRepository.findAllByCategoryName(product.getCategory().getName());
        assertTrue(allByCategoryName.isPresent());
        assertEquals(product, allByCategoryName.get().stream().findFirst().get());
    }
}