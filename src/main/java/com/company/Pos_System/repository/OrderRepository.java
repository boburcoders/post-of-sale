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

    @Query(value = """
            SELECT TO_CHAR(created_at, 'Mon') AS month,
                   SUM(total) AS revenue
            FROM orders
            WHERE EXTRACT(YEAR FROM created_at) = EXTRACT(YEAR FROM CURRENT_DATE)
              AND deleted_at IS NULL
            GROUP BY TO_CHAR(created_at, 'Mon'), EXTRACT(MONTH FROM created_at)
            ORDER BY EXTRACT(MONTH FROM created_at)
            """, nativeQuery = true)
    List<Object[]> getSalesOverview();

    @Query(value = """
            SELECT
                c.name AS category,
                ROUND(SUM(oi.price * oi.quantity) * 100.0 /
                    (SELECT SUM(oi2.price * oi2.quantity)
                     FROM order_items oi2
                     JOIN orders o2 ON oi2.order_id = o2.id
                     WHERE o2.deleted_at IS NULL AND oi2.deleted_at IS NULL), 2) AS percentage
            FROM order_items oi
            JOIN orders o ON oi.order_id = o.id
            JOIN products p ON oi.product_id = p.id
            JOIN category c ON p.category_id = c.id
            WHERE o.deleted_at IS NULL AND oi.deleted_at IS NULL
            GROUP BY c.name
            ORDER BY percentage DESC
            """, nativeQuery = true)
    List<Object[]> getSalesByCategory();


    @Query(value = """
            SELECT
                p.name,
                SUM(oi.quantity) AS sales
            FROM order_items oi
            JOIN products p ON oi.product_id = p.id
            JOIN orders o ON oi.order_id = o.id
            WHERE o.deleted_at IS NULL AND oi.deleted_at IS NULL
            GROUP BY p.name
            ORDER BY sales DESC
            LIMIT 5
            """, nativeQuery = true)
    List<Object[]> getTopSellingProducts();


}
