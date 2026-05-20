package org.example.bookcart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminAnalyticsResponse {

    // TOTAL REVENUE

    private Double totalRevenue;

    // TOTAL ORDERS

    private Long totalOrders;

    // LOW STOCK BOOKS

    private Long lowStockBooks;

    // MOST SOLD BOOK

    private String mostSoldBook;
}