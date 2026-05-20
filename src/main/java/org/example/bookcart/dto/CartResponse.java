package org.example.bookcart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CartResponse {

    private Long cartId;

    private List<CartItemResponse> items;

    private Double grandTotal;
}