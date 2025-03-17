package com.company.Pos_System.controller;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.UserDto;
import com.company.Pos_System.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;


    @PostMapping("/register")
    @Operation(summary = "Create a new User",description = "Create a new User")
    public HttpApiResponse<UserDto> registerUser(@RequestBody UserDto dto) {
        return this.userService.registerUser(dto);
    }

    @PostMapping("/login")
    @Operation(summary = "Login to Account",description = "Login with your Username and Password")
    public HttpApiResponse<UserDto> userLogin(@RequestBody UserDto dto) {
        return this.userService.userLogin(dto);
    }

    @GetMapping("/by-id/{id}")
    @Operation(summary = "Get user by Id",description = "Get User By Id")
    public HttpApiResponse<UserDto> getUserById(@PathVariable Long id) {
        return this.userService.getUserById(id);
    }

    @GetMapping("/by-username/{username}")
    @Operation(summary = "Get user by username",description = "Get User By username")
    public HttpApiResponse<List<UserDto>> findUsersByUsername(@PathVariable String username) {
        return this.userService.findUsersByUsername(username);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user by Id",description = "Update User By Id")
    public HttpApiResponse<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto dto) {
        return this.userService.updateUser(id, dto);
    }

    @PutMapping("/role/{id}")
    @Operation(summary = "Update userRole",description = "Update UserRole by Id")
    public HttpApiResponse<UserDto> updateUserRole(@PathVariable Long id, @RequestBody UserDto dto) {
        return this.userService.updateUserRole(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by Id",description = "Delete User By Id")
    public HttpApiResponse<String> deleteUserById(@PathVariable Long id) {
        return this.userService.deleteUserById(id);
    }

}
