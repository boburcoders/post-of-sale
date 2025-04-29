package com.company.Pos_System.repository;

import com.company.Pos_System.enums.OrderStatus;
import com.company.Pos_System.enums.UserRole;
import com.company.Pos_System.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderItemRepositoryTest {


    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Product product;
    Order order;
    Users user;
    OrderItem orderItem;
    Category category;

    @BeforeEach
    void setUp() {

        user = Users.builder()
                .fullName("Full Name")
                .username("activeUser")
                .password("<PASSWORD>")
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(user);

        category = Category.builder()
                .name("categoryName")
                .description("description")
                .build();
        categoryRepository.save(category);

        product = Product.builder()
                .name("Product Name")
                .serial("Serial")
                .price(BigDecimal.valueOf(122))
                .category(category)
                .description("Description")
                .build();
        productRepository.save(product);

        order = Order.builder()
                .status(OrderStatus.PENDING)
                .user(user)
                .total(BigDecimal.valueOf(1223))
                .build();
        orderRepository.save(order);

        orderItem = OrderItem.builder()
                .product(product)
                .quantity(BigDecimal.valueOf(1))
                .price(BigDecimal.valueOf(122))
                .order(order)
                .build();
        orderItemRepository.save(orderItem);


    }

    @Test
    void findByIdAndDeletedAtIsNull() {
        Optional<OrderItem> result = orderItemRepository.findByIdAndDeletedAtIsNull(orderItem.getId());
        assertTrue(result.isPresent());
        assertEquals(orderItem.getId(), result.get().getId());
    }

    @Test
    void findAllByDeletedAtIsNull() {
        Optional<List<OrderItem>> resultList = orderItemRepository.findAllByDeletedAtIsNull();
        assertTrue(resultList.isPresent());
        assertEquals(orderItem, resultList.get().get(0));
    }
}