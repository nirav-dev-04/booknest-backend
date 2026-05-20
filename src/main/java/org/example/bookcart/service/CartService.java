package org.example.bookcart.service;

import lombok.RequiredArgsConstructor;
import org.example.bookcart.dto.CartItemResponse;
import org.example.bookcart.dto.CartResponse;
import org.example.bookcart.entity.*;
import org.example.bookcart.exception.ResourceNotFoundException;
import org.example.bookcart.repository.BookRepository;
import org.example.bookcart.repository.CartItemRepository;
import org.example.bookcart.repository.CartRepository;
import org.example.bookcart.repository.UserRepository;
import org.example.bookcart.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final UserRepository userRepository;

    private final BookRepository bookRepository;

    // ADD BOOK TO CART
    public String addToCart(Long bookId) {

        // GET LOGGED-IN USER EMAIL
        String email =
                SecurityUtil.getCurrentUserEmail();

        // FIND USER
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User Not Found"
                        ));

        // FIND BOOK
        Book book = bookRepository
                .findById(bookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book Not Found"
                        ));

        // FIND USER CART
        Cart cart = cartRepository
                .findByUser(user)
                .orElseGet(() -> {

                    Cart newCart = new Cart();

                    newCart.setUser(user);

                    return cartRepository.save(newCart);
                });

        // CHECK IF BOOK ALREADY EXISTS
        CartItem cartItem =
                cartItemRepository
                        .findByCartAndBook(cart, book)
                        .orElse(null);

        // UPDATE QUANTITY
        if (cartItem != null) {

            cartItem.setQuantity(
                    cartItem.getQuantity() + 1
            );
        }

        // CREATE NEW CART ITEM
        else {

            cartItem = new CartItem();

            cartItem.setCart(cart);

            cartItem.setBook(book);

            cartItem.setQuantity(1);
        }

        cartItemRepository.save(cartItem);

        return "Book Added To Cart";
    }

    // GET USER CART
    public CartResponse getCart() {

        String email =
                SecurityUtil.getCurrentUserEmail();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User Not Found"
                        ));

        Cart cart = cartRepository
                .findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart Not Found"
                        ));

        double grandTotal = 0;

        List<CartItemResponse> items =
                cart.getCartItems()

                        .stream()

                        .map(cartItem -> {

                            double totalPrice =
                                    cartItem.getBook().getPrice()
                                            * cartItem.getQuantity();

                            return CartItemResponse.builder()

                                    .cartItemId(
                                            cartItem.getId()
                                    )

                                    .bookId(
                                            cartItem.getBook().getId()
                                    )

                                    .title(
                                            cartItem.getBook().getTitle()
                                    )

                                    .price(
                                            cartItem.getBook().getPrice()
                                    )

                                    .imageUrl(
                                            cartItem.getBook().getImageUrl()
                                    )

                                    .quantity(
                                            cartItem.getQuantity()
                                    )

                                    .totalPrice(
                                            totalPrice
                                    )

                                    .build();
                        })

                        .toList();

        // CALCULATE GRAND TOTAL
        for (CartItem cartItem : cart.getCartItems()) {

            grandTotal +=
                    cartItem.getBook().getPrice()
                            * cartItem.getQuantity();
        }

        return CartResponse.builder()

                .cartId(cart.getId())

                .items(items)

                .grandTotal(grandTotal)

                .build();
    }

    // REMOVE CART ITEM
    public String removeCartItem(
            Long cartItemId
    ) {

        CartItem cartItem =
                cartItemRepository.findById(cartItemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cart Item Not Found"
                                ));

        cartItemRepository.delete(cartItem);

        return "Cart Item Removed";
    }

    // CLEAR CART
    public String clearCart() {

        String email =
                SecurityUtil.getCurrentUserEmail();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User Not Found"
                        ));

        Cart cart = cartRepository
                .findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart Not Found"
                        ));

        cart.getCartItems().clear();

        cartRepository.save(cart);

        return "Cart Cleared";
    }
}