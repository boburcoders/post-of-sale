package com.company.Pos_System.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WareHouseDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String location;

}
