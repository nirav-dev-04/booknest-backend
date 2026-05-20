package org.example.bookcart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // MANY ORDER ITEMS -> ONE ORDER
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // MANY ORDER ITEMS -> ONE BOOK
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private Integer quantity;

    private Double price;
}