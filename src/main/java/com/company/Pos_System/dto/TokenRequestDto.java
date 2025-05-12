package com.company.Pos_System.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TokenRequestDto {
    private String username;
    private String password;
}
