package com.company.Pos_System.repository;

import com.company.Pos_System.models.WareHouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WareHouseRepositoryTest {
    @Autowired
    WareHouseRepository wareHouseRepository;

    WareHouse wareHouse;

    @BeforeEach
    void setUp() {
        wareHouse = WareHouse.builder()
                .name("Warehouse Name")
                .location("Location")
                .build();
        wareHouseRepository.save(wareHouse);
    }

    @Test
    void findByIdAndDeletedAtIsNull() {
        Optional<WareHouse> result = wareHouseRepository.findByIdAndDeletedAtIsNull(wareHouse.getId());
        assertTrue(result.isPresent());
        assertEquals(wareHouse.getId(), result.get().getId());
    }

    @Test
    void findAllByDeletedAtIsNull() {
        Optional<List<WareHouse>> resultList = wareHouseRepository.findAllByDeletedAtIsNull();
        assertTrue(resultList.isPresent());
        assertEquals(wareHouse, resultList.get().get(0));
    }
}