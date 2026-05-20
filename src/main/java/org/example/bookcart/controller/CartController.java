package org.example.bookcart.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookcart.dto.CartResponse;
import org.example.bookcart.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // ADD TO CART
    @PostMapping("/add/{bookId}")
    public String addToCart(
            @PathVariable Long bookId
    ) {

        return cartService.addToCart(bookId);
    }

    // GET CART
    @GetMapping
    public CartResponse getCart() {

        return cartService.getCart();
    }

    // REMOVE CART ITEM
    @DeleteMapping("/remove/{cartItemId}")
    public String removeCartItem(
            @PathVariable Long cartItemId
    ) {

        return cartService.removeCartItem(
                cartItemId
        );
    }

    // CLEAR CART
    @DeleteMapping("/clear")
    public String clearCart() {

        return cartService.clearCart();
    }
}