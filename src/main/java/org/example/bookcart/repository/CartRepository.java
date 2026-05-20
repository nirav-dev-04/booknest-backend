package org.example.bookcart.repository;

import org.example.bookcart.entity.Cart;
import org.example.bookcart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository
        extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}