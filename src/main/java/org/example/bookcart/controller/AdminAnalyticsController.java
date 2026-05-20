package org.example.bookcart.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookcart.dto.AdminAnalyticsResponse;
import org.example.bookcart.dto.ApiResponse;
import org.example.bookcart.service.BookService;
import org.example.bookcart.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/analytics")
@RequiredArgsConstructor
public class AdminAnalyticsController {

    private final OrderService orderService;

    private final BookService bookService;

    // GET ANALYTICS

    @GetMapping
    public ResponseEntity<ApiResponse<AdminAnalyticsResponse>>
    getAnalytics() {

        AdminAnalyticsResponse analytics =

                AdminAnalyticsResponse.builder()

                        // TOTAL REVENUE

                        .totalRevenue(
                                orderService.getTotalRevenue()
                        )

                        // TOTAL ORDERS

                        .totalOrders(
                                orderService.getTotalOrders()
                        )

                        // LOW STOCK BOOKS

                        .lowStockBooks(
                                bookService.getLowStockBooksCount()
                        )

                        // MOST SOLD BOOK

                        .mostSoldBook(
                                bookService.getMostSoldBook()
                        )

                        .build();

        return ResponseEntity.ok(

                ApiResponse
                        .<AdminAnalyticsResponse>builder()

                        .success(true)

                        .message(
                                "Analytics fetched successfully"
                        )

                        .data(analytics)

                        .build()
        );
    }
}