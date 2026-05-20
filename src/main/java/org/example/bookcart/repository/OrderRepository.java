package org.example.bookcart.repository;

import org.example.bookcart.entity.Order;
import org.example.bookcart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    // FIND USER ORDERS

    List<Order> findByUser(User user);

    // TOTAL REVENUE

    @Query(
            "SELECT COALESCE(SUM(o.totalAmount), 0) " +
                    "FROM Order o"
    )
    Double getTotalRevenue();

    // TOTAL ORDERS

    @Query(
            "SELECT COUNT(o) FROM Order o"
    )
    Long getTotalOrders();
}