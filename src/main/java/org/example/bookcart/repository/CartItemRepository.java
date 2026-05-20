package org.example.bookcart.repository;

import org.example.bookcart.entity.Book;
import org.example.bookcart.entity.Cart;
import org.example.bookcart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndBook(
            Cart cart,
            Book book
    );
}