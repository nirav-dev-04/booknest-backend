package org.example.bookcart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AdminOrderResponse {

    private Long orderId;

    private String customerName;

    private String customerEmail;

    private Double totalAmount;

    private String status;

    private LocalDateTime orderDate;
}