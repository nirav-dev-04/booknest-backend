package org.example.bookcart.service;

import lombok.RequiredArgsConstructor;
import org.example.bookcart.dto.AdminOrderResponse;
import org.example.bookcart.dto.OrderResponse;
import org.example.bookcart.entity.*;
import org.example.bookcart.exception.ResourceNotFoundException;
import org.example.bookcart.repository.CartRepository;
import org.example.bookcart.repository.OrderRepository;
import org.example.bookcart.repository.UserRepository;
import org.example.bookcart.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final OrderRepository orderRepository;

    // PLACE ORDER

    @Transactional
    public String placeOrder() {

        // GET LOGGED-IN USER EMAIL

        String email =
                SecurityUtil.getCurrentUserEmail();

        // FIND USER

        User user = userRepository
                .findByEmail(email)

                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "User Not Found"
                        )
                );

        // FIND CART

        Cart cart = cartRepository
                .findByUser(user)

                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "Cart Not Found"
                        )
                );

        // EMPTY CART CHECK

        if (cart.getCartItems().isEmpty()) {

            throw new RuntimeException(
                    "Cart Is Empty"
            );
        }

        // CREATE ORDER

        Order order = new Order();

        order.setUser(user);

        order.setOrderDate(
                LocalDateTime.now()
        );

        order.setStatus(
                OrderStatus.PENDING
        );

        List<OrderItem> orderItems =
                new ArrayList<>();

        double totalAmount = 0;

        // LOOP CART ITEMS

        for (CartItem cartItem
                : cart.getCartItems()) {

            Book book =
                    cartItem.getBook();

            // INVALID QUANTITY CHECK

            if (cartItem.getQuantity() <= 0) {

                throw new RuntimeException(
                        "Invalid Quantity"
                );
            }

            // OUT OF STOCK CHECK

            if (book.getStock() <= 0) {

                throw new RuntimeException(

                        "Book Out Of Stock: "

                                + book.getTitle()
                );
            }

            // AVAILABLE STOCK CHECK

            if (cartItem.getQuantity()
                    > book.getStock()) {

                throw new RuntimeException(

                        "Only "

                                + book.getStock()

                                + " items available for "

                                + book.getTitle()
                );
            }

            // REDUCE STOCK

            book.setStock(

                    book.getStock()

                            - cartItem.getQuantity()
            );

            // CREATE ORDER ITEM

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrder(order);

            orderItem.setBook(book);

            orderItem.setQuantity(
                    cartItem.getQuantity()
            );

            orderItem.setPrice(
                    book.getPrice()
            );

            orderItems.add(orderItem);

            // CALCULATE TOTAL

            totalAmount +=

                    book.getPrice()

                            * cartItem.getQuantity();
        }

        // SET ORDER ITEMS

        order.setOrderItems(orderItems);

        // SET TOTAL AMOUNT

        order.setTotalAmount(totalAmount);

        // SAVE ORDER

        orderRepository.save(order);

        // CLEAR CART

        cart.getCartItems().clear();

        cartRepository.save(cart);

        return "Order Placed Successfully";
    }

    // GET MY ORDERS

    public List<OrderResponse> getMyOrders() {

        // GET LOGGED-IN USER EMAIL

        String email =
                SecurityUtil.getCurrentUserEmail();

        // FIND USER

        User user = userRepository
                .findByEmail(email)

                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "User Not Found"
                        )
                );

        // FIND USER ORDERS

        List<Order> orders =
                orderRepository.findByUser(user);

        return orders.stream()

                .map(order ->

                        OrderResponse.builder()

                                .orderId(
                                        order.getId()
                                )

                                .totalAmount(
                                        order.getTotalAmount()
                                )

                                .status(
                                        order.getStatus().name()
                                )

                                .orderDate(
                                        order.getOrderDate()
                                )

                                .build()
                )

                .toList();
    }

    // ADMIN - GET ALL ORDERS

    public List<AdminOrderResponse>
    getAllOrders() {

        List<Order> orders =
                orderRepository.findAll();

        return orders.stream()

                .map(order ->

                        AdminOrderResponse.builder()

                                .orderId(
                                        order.getId()
                                )

                                .customerName(
                                        order.getUser().getName()
                                )

                                .customerEmail(
                                        order.getUser().getEmail()
                                )

                                .totalAmount(
                                        order.getTotalAmount()
                                )

                                .status(
                                        order.getStatus().name()
                                )

                                .orderDate(
                                        order.getOrderDate()
                                )

                                .build()
                )

                .toList();
    }

    // ADMIN - UPDATE ORDER STATUS

    public String updateOrderStatus(

            Long orderId,

            OrderStatus status
    ) {

        Order order =
                orderRepository.findById(orderId)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Order Not Found"
                                )
                        );

        order.setStatus(status);

        orderRepository.save(order);

        return "Order Status Updated Successfully";
    }

    // TOTAL REVENUE

    public Double getTotalRevenue() {

        Double revenue =

                orderRepository
                        .getTotalRevenue();

        return revenue != null
                ? revenue
                : 0.0;
    }

    // TOTAL ORDERS

    public Long getTotalOrders() {

        return orderRepository
                .getTotalOrders();
    }
}