package com.company.Pos_System.dto;

import com.company.Pos_System.enums.UserRole;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(fullName, userDto.fullName) && Objects.equals(username, userDto.username) && Objects.equals(password, userDto.password) && role == userDto.role && Objects.equals(orders, userDto.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, username, password, role, orders);
    }
}
