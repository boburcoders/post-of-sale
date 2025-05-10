package com.company.Pos_System.repository;

import com.company.Pos_System.dto.RevenueStatsDto;
import com.company.Pos_System.dto.SalesTodayDto;
import com.company.Pos_System.dto.TotalOrdersDto;
import com.company.Pos_System.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndDeletedAtIsNull(Long id);

    Optional<List<Order>> findAllByDeletedAtIsNull();


    @Query(value = """
            SELECT
              ROUND(COALESCE(this_month.total, 0), 2) AS totalRevenue,
              ROUND(
                CASE
                  WHEN last_month.total IS NULL OR last_month.total = 0 THEN 100
                  ELSE ((this_month.total - last_month.total) / last_month.total) * 100
                END, 1
              ) AS revenueGrowth,
              CASE
                WHEN COALESCE(this_month.total, 0) > COALESCE(last_month.total, 0) THEN 'up'
                WHEN COALESCE(this_month.total, 0) < COALESCE(last_month.total, 0) THEN 'down'
                ELSE 'same'
              END AS indicator
            FROM
              (
                SELECT SUM(total) AS total
                FROM orders
                WHERE DATE_TRUNC('month', created_at) = DATE_TRUNC('month', CURRENT_DATE)
                  AND deleted_at IS NULL
              ) AS this_month,
              (
                SELECT SUM(total) AS total
                FROM orders
                WHERE DATE_TRUNC('month', created_at) = DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month')
                  AND deleted_at IS NULL
              ) AS last_month;
            """, nativeQuery = true)
    List<Object[]> getRevenueStatsRaw();


    @Query(value = """
            SELECT
              ROUND(COALESCE(today.total, 0), 2) AS salesToday,
              ROUND(
                CASE
                  WHEN yesterday.total IS NULL OR yesterday.total = 0 THEN 100
                  ELSE ((today.total - yesterday.total) / yesterday.total) * 100
                END, 1
              ) AS salesGrowth,
              CASE
                WHEN COALESCE(today.total, 0) > COALESCE(yesterday.total, 0) THEN 'up'
                WHEN COALESCE(today.total, 0) < COALESCE(yesterday.total, 0) THEN 'down'
                ELSE 'same'
              END AS indicator
            FROM
              (
                SELECT SUM(total) AS total
                FROM orders
                WHERE DATE(created_at) = CURRENT_DATE
                  AND deleted_at IS NULL
              ) AS today,
              (
                SELECT SUM(total) AS total
                FROM orders
                WHERE DATE(created_at) = CURRENT_DATE - INTERVAL '1 day'
                  AND deleted_at IS NULL
              ) AS yesterday;
            """, nativeQuery = true)
    List<Object[]> getSalesTodayRaw();


    @Query(value = """ 
            SELECT
              COALESCE(this_month.total, 0) AS total_orders,
              ROUND(
                CASE
                  WHEN last_month.total IS NULL OR last_month.total = 0 THEN 100
                  ELSE ((this_month.total - last_month.total) / last_month.total::decimal) * 100
                END, 1
              ) AS orders_growth,
              CASE
                WHEN COALESCE(this_month.total, 0) > COALESCE(last_month.total, 0) THEN 'up'
                WHEN COALESCE(this_month.total, 0) < COALESCE(last_month.total, 0) THEN 'down'
                ELSE 'same'
              END AS indicator
            FROM
              (
                SELECT COUNT(*) AS total
                FROM orders
                WHERE DATE_TRUNC('month', created_at) = DATE_TRUNC('month', CURRENT_DATE)
                  AND deleted_at IS NULL
              ) AS this_month,
              (
                SELECT COUNT(*) AS total
                FROM orders
                WHERE DATE_TRUNC('month', created_at) = DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month')
                  AND deleted_at IS NULL
              ) AS last_month;
            """, nativeQuery = true)
    List<Object[]> getTotalOrdersStatsRaw();


}
