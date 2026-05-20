package org.example.bookcart.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookcart.dto.AdminOrderResponse;
import org.example.bookcart.dto.ApiResponse;
import org.example.bookcart.dto.UpdateOrderStatusRequest;
import org.example.bookcart.service.AdminOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    // GET ALL ORDERS

    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminOrderResponse>>>
    getAllOrders() {

        return ResponseEntity.ok(

                ApiResponse
                        .<List<AdminOrderResponse>>builder()

                        .success(true)

                        .message(
                                "All Orders Fetched Successfully"
                        )

                        .data(
                                adminOrderService.getAllOrders()
                        )

                        .build()
        );
    }

    // UPDATE ORDER STATUS

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<Object>>
    updateOrderStatus(

            @PathVariable Long orderId,

            @RequestBody
            UpdateOrderStatusRequest request
    ) {

        return ResponseEntity.ok(

                ApiResponse.builder()

                        .success(true)

                        .message(

                                adminOrderService
                                        .updateOrderStatus(
                                                orderId,
                                                request
                                        )
                        )

                        .data(null)

                        .build()
        );
    }
}