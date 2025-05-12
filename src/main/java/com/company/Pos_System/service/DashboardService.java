package com.company.Pos_System.service;

import com.company.Pos_System.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DashboardService {
    HttpApiResponse<RevenueStatsDto> getRevenueStats();

    HttpApiResponse<SalesTodayDto> getSalesTodayStats();

    HttpApiResponse<TotalOrdersDto> getTotalOrdersStats();

    HttpApiResponse<List<SalesOverviewDto>> getSalesOverview();

    HttpApiResponse<List<SalesByCategoryDto>> getSalesByCategory();

    HttpApiResponse<List<TopSellingProductDto>> getTopSellingProducts();
}
