package com.company.Pos_System.controller;

import com.company.Pos_System.config.UserPrincipal;
import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.LoginResponseDto;
import com.company.Pos_System.dto.TokenRequestDto;
import com.company.Pos_System.dto.UserDto;
import com.company.Pos_System.config.JwtTokenUtil;
import com.company.Pos_System.models.Users;
import com.company.Pos_System.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
@EnableMethodSecurity
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;


    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new User", description = "Create a new User")
    public HttpApiResponse<UserDto> registerUser(@RequestBody UserDto dto) {
        return this.userService.registerUser(dto);
    }

    @PostMapping("/login")
    @Operation(summary = "Get JWT Token", description = "Get Token with your Username and Password")
    public HttpApiResponse<LoginResponseDto> userLogin(@RequestBody TokenRequestDto dto) {
        return userService.userLogin(dto);
    }



    @GetMapping("/by-id/{id}")
    @Operation(summary = "Get user by Id", description = "Get User By Id")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public HttpApiResponse<UserDto> getUserById(@PathVariable Long id) {
        return this.userService.getUserById(id);
    }

    @GetMapping("/by-username/{username}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Get user by username", description = "Get User By username")
    public HttpApiResponse<List<UserDto>> findUsersByUsername(@PathVariable String username) {
        return this.userService.findUsersByUsername(username);
    }

    @Operation(summary = "Get all users", description = "Get all user list")
    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public HttpApiResponse<List<UserDto>> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user by Id", description = "Update User By Id")
    public HttpApiResponse<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto dto) {
        return this.userService.updateUser(id, dto);
    }

    @PutMapping("/role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update userRole", description = "Update UserRole by Id")
    public HttpApiResponse<UserDto> updateUserRole(@PathVariable Long id, @RequestBody UserDto dto) {
        return this.userService.updateUserRole(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user by Id", description = "Delete User By Id")
    public HttpApiResponse<String> deleteUserById(@PathVariable Long id) {
        return this.userService.deleteUserById(id);
    }

}
