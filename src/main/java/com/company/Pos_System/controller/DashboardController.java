package com.company.Pos_System.controller;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.RevenueStatsDto;
import com.company.Pos_System.dto.SalesTodayDto;
import com.company.Pos_System.dto.TotalOrdersDto;
import com.company.Pos_System.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
@EnableMethodSecurity
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/revenue")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public HttpApiResponse<RevenueStatsDto> getRevenueStats() {
        return dashboardService.getRevenueStats();
    }

    @GetMapping("/sales-today")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public HttpApiResponse<SalesTodayDto> getSalesTodayStats() {
        return dashboardService.getSalesTodayStats();
    }

    @GetMapping("/total-orders")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public HttpApiResponse<TotalOrdersDto> getTotalOrdersStats() {
        return dashboardService.getTotalOrdersStats();
    }
}
