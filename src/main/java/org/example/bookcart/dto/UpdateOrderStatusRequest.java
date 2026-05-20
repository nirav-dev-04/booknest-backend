package org.example.bookcart.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.bookcart.entity.OrderStatus;

@Getter
@Setter
public class UpdateOrderStatusRequest {

    private OrderStatus status;
}