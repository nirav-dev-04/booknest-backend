package org.example.bookcart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MostSoldBookResponse {

    private String title;

    private Long totalSold;
}