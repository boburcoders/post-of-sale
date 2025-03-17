package com.company.Pos_System.repository;

import com.company.Pos_System.models.Category;
import com.company.Pos_System.models.Product;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndDeletedAtIsNull(Long id);

    Optional<Category> findByNameAndDeletedAtIsNull(String name);

    boolean existsByIdAndDeletedAtIsNull(Long id);

    boolean existsByNameAndDeletedAtIsNull(String name);

    Optional<List<Category>> findAllByDeletedAtIsNull();

}
