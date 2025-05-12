package com.company.Pos_System.models;

import jakarta.persistence.Entity;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class WareHouse extends BaseEntity {

    private String name;
    private String location;
}
