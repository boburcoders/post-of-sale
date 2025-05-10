package com.company.Pos_System.repository;

import com.company.Pos_System.models.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WareHouseRepository extends JpaRepository<WareHouse, Long> {
    Optional<WareHouse> findByIdAndDeletedAtIsNull(Long id);

    Optional<List<WareHouse>> findAllByDeletedAtIsNull();

}
