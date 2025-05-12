package com.company.Pos_System.repository;

import com.company.Pos_System.models.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name("categoryName")
                .description("description")
                .build();
        categoryRepository.save(category);
    }

    @Test
    void findByIdAndDeletedAtIsNull() {
        Optional<Category> result = categoryRepository.findByIdAndDeletedAtIsNull(category.getId());
        assertTrue(result.isPresent());
        assertEquals(category.getId(), result.get().getId());
    }

    @Test
    void findByNameAndDeletedAtIsNull() {
        Optional<Category> result = categoryRepository.findByNameAndDeletedAtIsNull(category.getName());
        assertTrue(result.isPresent());
        assertEquals(category.getName(), result.get().getName());
    }


    @Test
    void findAllByDeletedAtIsNull() {
        Optional<List<Category>> resultList = categoryRepository.findAllByDeletedAtIsNull();
        assertTrue(resultList.isPresent());
        assertEquals(category, resultList.get().get(0));
    }
}