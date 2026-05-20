package org.example.bookcart.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // MANY CART ITEMS BELONG TO ONE CART
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    // MANY CART ITEMS CAN REFER TO ONE BOOK
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private Integer quantity;
}