package com.company.Pos_System.service;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.RevenueStatsDto;
import com.company.Pos_System.dto.SalesTodayDto;
import com.company.Pos_System.dto.TotalOrdersDto;
import org.springframework.stereotype.Service;

@Service
public interface DashboardService {
    HttpApiResponse<RevenueStatsDto> getRevenueStats();

    HttpApiResponse<SalesTodayDto> getSalesTodayStats();

    HttpApiResponse<TotalOrdersDto> getTotalOrdersStats();
}
