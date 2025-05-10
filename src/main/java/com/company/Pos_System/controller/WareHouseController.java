package com.company.Pos_System.controller;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.WareHouseDto;
import com.company.Pos_System.service.WareHouseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/wareHouse")
@EnableMethodSecurity
public class WareHouseController {
    private final WareHouseService wareHouseService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Create a new WareHouse", description = "Create a new WareHouse")
    public HttpApiResponse<WareHouseDto> createWareHouse(@RequestBody @Valid WareHouseDto dto) {
        return wareHouseService.createWareHouse(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Find a WareHouse by Id", description = "Find  a new WareHouse by Id")
    public HttpApiResponse<WareHouseDto> getWareHouseById(@PathVariable("id") Long id) {
        return wareHouseService.getWareHouseById(id);
    }

    @GetMapping("/get-all")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get All WareHouse", description = "Get All WareHouse")
    public HttpApiResponse<List<WareHouseDto>> getAllWareHouses() {
        return wareHouseService.getAllWareHouse();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Update a WareHouse by Id", description = "Update a WareHouse by Id")
    public HttpApiResponse<WareHouseDto> updateWareHouseById(@PathVariable("id") Long id, @RequestBody WareHouseDto dto) {
        return wareHouseService.updateWareHouseById(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Delete a WareHouse by Id", description = "Delete a WareHouse by Id")
    public HttpApiResponse<String> deleteWareHouseById(@PathVariable("id") Long id) {
        return wareHouseService.deleteWareHouseById(id);
    }
}
