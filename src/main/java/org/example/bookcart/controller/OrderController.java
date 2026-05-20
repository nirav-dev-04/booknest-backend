package org.example.bookcart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.bookcart.dto.ApiResponse;
import org.example.bookcart.dto.OrderResponse;
import org.example.bookcart.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor

@Tag(
        name = "Order APIs",
        description = "Operations related to orders"
)
public class OrderController {

    private final OrderService orderService;

    // PLACE ORDER
    @Operation(summary = "Place order from cart")
    @PostMapping("/place")
    public ResponseEntity<ApiResponse<Object>>
    placeOrder() {

        return ResponseEntity.ok(

                ApiResponse.builder()

                        .success(true)

                        .message(
                                orderService.placeOrder()
                        )

                        .data(null)

                        .build()
        );
    }

    // GET MY ORDER HISTORY
    @Operation(summary = "Get logged-in user order history")
    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>>
    getMyOrders() {

        return ResponseEntity.ok(

                ApiResponse.<List<OrderResponse>>builder()

                        .success(true)

                        .message("Orders fetched successfully")

                        .data(
                                orderService.getMyOrders()
                        )

                        .build()
        );
    }
}