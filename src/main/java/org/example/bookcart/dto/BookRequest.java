package org.example.bookcart.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    @NotNull(message = "Price is required")
    @DecimalMin(
            value = "1.0",
            message = "Price must be greater than 0"
    )
    private Double price;

    @NotNull(message = "Stock is required")
    @Min(
            value = 0,
            message = "Stock cannot be negative"
    )
    private Integer stock;
}