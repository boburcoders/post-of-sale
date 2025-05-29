package com.company.Pos_System.controller;

import com.company.Pos_System.dto.*;
import com.company.Pos_System.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/revenue")
    public HttpApiResponse<RevenueStatsDto> getRevenueStats() {
        return dashboardService.getRevenueStats();
    }

    @GetMapping("/sales-today")
    public HttpApiResponse<SalesTodayDto> getSalesTodayStats() {
        return dashboardService.getSalesTodayStats();
    }

    @GetMapping("/total-orders")
    public HttpApiResponse<TotalOrdersDto> getTotalOrdersStats() {
        return dashboardService.getTotalOrdersStats();
    }

    @GetMapping("/sales-overview")
    public HttpApiResponse<List<SalesOverviewDto>> getSalesOverview() {
        return dashboardService.getSalesOverview();
    }

    @GetMapping("/sales-by-category")
    public HttpApiResponse<List<SalesByCategoryDto>> getSalesByCategory() {
        return dashboardService.getSalesByCategory();
    }

    @GetMapping("/top-products")
    public HttpApiResponse<List<TopSellingProductDto>> getTopSellingProducts() {
        return dashboardService.getTopSellingProducts();
    }


}
