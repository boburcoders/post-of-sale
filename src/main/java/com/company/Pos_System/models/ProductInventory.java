package com.company.Pos_System.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class ProductInventory extends BaseEntity {
    @ManyToOne
    private Product product;

    @ManyToOne
    private WareHouse warehouse;
    private int quantity; // number of items in stock
}
