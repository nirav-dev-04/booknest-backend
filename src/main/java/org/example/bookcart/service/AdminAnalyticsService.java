package org.example.bookcart.service;

import lombok.RequiredArgsConstructor;
import org.example.bookcart.dto.AdminAnalyticsResponse;
import org.example.bookcart.dto.MostSoldBookResponse;
import org.example.bookcart.repository.BookRepository;
import org.example.bookcart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAnalyticsService {

    private final OrderRepository orderRepository;

    private final BookRepository bookRepository;

    // GET DASHBOARD ANALYTICS

    public AdminAnalyticsResponse
    getAnalytics() {

        // TOTAL REVENUE

        Double totalRevenue =
                orderRepository.getTotalRevenue();

        // TOTAL ORDERS

        Long totalOrders =
                orderRepository.getTotalOrders();

        // LOW STOCK BOOKS

        Long lowStockBooks =
                bookRepository
                        .getLowStockBooksCount();

        // MOST SOLD BOOK

        String mostSoldBook =
                bookRepository
                        .getMostSoldBook();

        // NULL CHECK

        if (mostSoldBook == null) {

            mostSoldBook =
                    "No Sales Yet";
        }

        return AdminAnalyticsResponse.builder()

                .totalRevenue(totalRevenue)

                .totalOrders(totalOrders)

                .lowStockBooks(lowStockBooks)

                .mostSoldBook(mostSoldBook)

                .build();
    }

    // GET MOST SOLD BOOKS

    public List<MostSoldBookResponse>
    getMostSoldBooks() {

        return bookRepository
                .getMostSoldBooks();
    }
}