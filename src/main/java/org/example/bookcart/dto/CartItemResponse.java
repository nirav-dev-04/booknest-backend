package org.example.bookcart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CartItemResponse {

    private Long cartItemId;

    private Long bookId;

    private String title;

    private Double price;

    private Integer quantity;

    private Double totalPrice;

    private String imageUrl;
}