package org.example.bookcart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;

    private Double totalAmount;

    private String status;

    private LocalDateTime orderDate;
}