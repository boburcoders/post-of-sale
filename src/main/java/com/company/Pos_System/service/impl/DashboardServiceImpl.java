package com.company.Pos_System.service.impl;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.RevenueStatsDto;
import com.company.Pos_System.dto.SalesTodayDto;
import com.company.Pos_System.dto.TotalOrdersDto;
import com.company.Pos_System.repository.OrderRepository;
import com.company.Pos_System.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final OrderRepository orderRepository;

    @Override
    public HttpApiResponse<RevenueStatsDto> getRevenueStats() {
        try {
            return HttpApiResponse.<RevenueStatsDto>builder()
                    .success(true)
                    .message("OK")
                    .status(HttpStatus.OK)
                    .data(getRevenueStatsFromDb())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public HttpApiResponse<SalesTodayDto> getSalesTodayStats() {
        try {
            return HttpApiResponse.<SalesTodayDto>builder()
                    .success(true)
                    .message("OK")
                    .status(HttpStatus.OK)
                    .data(getSalesTodayFromDb())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public HttpApiResponse<TotalOrdersDto> getTotalOrdersStats() {
        try {
            return HttpApiResponse.<TotalOrdersDto>builder()
                    .success(true)
                    .message("OK")
                    .status(HttpStatus.OK)
                    .data(getTotalOrderFromDb())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }


    public TotalOrdersDto getTotalOrderFromDb() {
        List<Object[]> result = orderRepository.getTotalOrdersStatsRaw();

        if (result.isEmpty()) {
            return new TotalOrdersDto(0L, BigDecimal.ZERO, "same");
        }

        Object[] row = result.get(0); // First row from the result

        Long totalOrders = ((Number) row[0]).longValue();             // column 1
        BigDecimal ordersGrowth = new BigDecimal(row[1].toString());  // column 2
        String indicator = (String) row[2];                            // column 3

        return new TotalOrdersDto(totalOrders, ordersGrowth, indicator);
    }


    public SalesTodayDto getSalesTodayFromDb() {
        Object[] row = orderRepository.getSalesTodayRaw().get(0);

        BigDecimal salesToday = new BigDecimal(row[0].toString());
        BigDecimal growth = new BigDecimal(row[1].toString());
        String indicator = (String) row[2];

        return new SalesTodayDto(salesToday, growth, indicator);
    }


    public RevenueStatsDto getRevenueStatsFromDb() {
        Object[] row = orderRepository.getRevenueStatsRaw().get(0);

        BigDecimal totalRevenue = new BigDecimal(row[0].toString());
        BigDecimal growth = new BigDecimal(row[1].toString());
        String indicator = row[2].toString();

        return new RevenueStatsDto(totalRevenue, growth, indicator);
    }

}
