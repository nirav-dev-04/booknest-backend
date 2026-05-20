package org.example.bookcart.service;

import lombok.RequiredArgsConstructor;
import org.example.bookcart.dto.AdminOrderResponse;
import org.example.bookcart.dto.UpdateOrderStatusRequest;
import org.example.bookcart.entity.Order;
import org.example.bookcart.entity.OrderStatus;
import org.example.bookcart.exception.ResourceNotFoundException;
import org.example.bookcart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final OrderRepository orderRepository;

    // GET ALL ORDERS

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

    // UPDATE ORDER STATUS

    public String updateOrderStatus(

            Long orderId,

            UpdateOrderStatusRequest request
    ) {

        Order order =
                orderRepository.findById(orderId)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Order Not Found"
                                )
                        );

        OrderStatus currentStatus =
                order.getStatus();

        OrderStatus newStatus =
                request.getStatus();

        // PREVENT MODIFICATION OF COMPLETED ORDERS

        if (currentStatus == OrderStatus.DELIVERED) {

            throw new RuntimeException(
                    "Delivered order cannot be modified"
            );
        }

        // PREVENT MODIFICATION OF CANCELLED ORDERS

        if (currentStatus == OrderStatus.CANCELLED) {

            throw new RuntimeException(
                    "Cancelled order cannot be modified"
            );
        }

        // VALID STATUS FLOW

        boolean isValidTransition = switch (currentStatus) {

            case PENDING ->

                    newStatus == OrderStatus.CONFIRMED
                            || newStatus == OrderStatus.CANCELLED;

            case CONFIRMED ->

                    newStatus == OrderStatus.SHIPPED
                            || newStatus == OrderStatus.CANCELLED;

            case SHIPPED ->

                    newStatus == OrderStatus.DELIVERED;

            default -> false;
        };

        // INVALID STATUS CHANGE

        if (!isValidTransition) {

            throw new RuntimeException(
                    "Invalid Order Status Transition"
            );
        }

        // UPDATE STATUS

        order.setStatus(newStatus);

        orderRepository.save(order);

        return "Order Status Updated Successfully";
    }
}