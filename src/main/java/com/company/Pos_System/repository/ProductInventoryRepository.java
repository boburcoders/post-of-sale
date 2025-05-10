package com.company.Pos_System.repository;

import com.company.Pos_System.models.Product;
import com.company.Pos_System.models.ProductInventory;
import com.company.Pos_System.models.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {
    Optional<ProductInventory> findByIdAndDeletedAtIsNull(Long id);

    Optional<List<ProductInventory>> findAllByDeletedAtIsNull();

    Optional<ProductInventory> findByProductAndWarehouse(Product product, WareHouse warehouse);

}
