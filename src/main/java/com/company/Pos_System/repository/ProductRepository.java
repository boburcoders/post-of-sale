package com.company.Pos_System.repository;

import com.company.Pos_System.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByIdAndDeletedAtIsNull(Long id);

    Optional<Set<Product>> findAllByDeletedAtIsNull();

    Optional<Set<Product>> findAllByCategoryName(String categoryName);
}
