package com.company.Pos_System.dto;

import com.company.Pos_System.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String fullName;
    private String username;
    private String password;
    private UserRole role;
    private List<OrdersDto> orders = new ArrayList<>();

}
