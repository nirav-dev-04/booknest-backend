package org.example.bookcart.repository;

import org.example.bookcart.dto.MostSoldBookResponse;
import org.example.bookcart.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository
        extends JpaRepository<Book, Long> {

    // SEARCH BY CATEGORY

    List<Book> findByCategory(
            String category
    );

    // SEARCH BY TITLE

    List<Book> findByTitleContainingIgnoreCase(
            String keyword
    );

    // FILTER BOOKS

    List<Book> findByCategoryAndPriceBetween(

            String category,

            Double minPrice,

            Double maxPrice
    );

    // LOW STOCK BOOKS

    @Query(
            "SELECT COUNT(b) " +
                    "FROM Book b " +
                    "WHERE b.stock < 5"
    )
    Long getLowStockBooksCount();

    // MOST SOLD BOOKS

    @Query(

            "SELECT new org.example.bookcart.dto.MostSoldBookResponse(" +

                    "oi.book.title, " +

                    "SUM(oi.quantity)" +

                    ") " +

                    "FROM OrderItem oi " +

                    "GROUP BY oi.book.title " +

                    "ORDER BY SUM(oi.quantity) DESC"
    )
    List<MostSoldBookResponse>
    getMostSoldBooks();

    // SINGLE MOST SOLD BOOK

    @Query(

            value =

                    "SELECT b.title " +

                            "FROM order_item oi " +

                            "JOIN books b " +

                            "ON oi.book_id = b.id " +

                            "GROUP BY b.title " +

                            "ORDER BY SUM(oi.quantity) DESC " +

                            "LIMIT 1",

            nativeQuery = true
    )
    String getMostSoldBook();
}