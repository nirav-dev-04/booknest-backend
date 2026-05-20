package org.example.bookcart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookcart.dto.ApiResponse;
import org.example.bookcart.dto.BookRequest;
import org.example.bookcart.dto.BookResponse;
import org.example.bookcart.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor

@Tag(
        name = "Book APIs",
        description = "Operations related to books"
)
public class BookController {

    private final BookService bookService;

    // ADD BOOK

    @Operation(summary = "Add a new book")
    @PostMapping
    public ResponseEntity<ApiResponse<Object>>
    addBook(

            @Valid
            @RequestBody
            BookRequest request
    ) {

        String response =
                bookService.addBook(request);

        return ResponseEntity.ok(

                ApiResponse.builder()

                        .success(true)

                        .message(response)

                        .data(null)

                        .build()
        );
    }

    // GET ALL BOOKS

    @Operation(summary = "Get all books with pagination")
    @GetMapping
    public ResponseEntity<
            ApiResponse<Page<BookResponse>>
            >
    getAllBooks(

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "5"
            )
            int size,

            @RequestParam(
                    defaultValue = "title"
            )
            String sortBy,

            @RequestParam(
                    defaultValue = "asc"
            )
            String direction
    ) {

        return ResponseEntity.ok(

                ApiResponse
                        .<Page<BookResponse>>
                                builder()

                        .success(true)

                        .message(
                                "Books fetched successfully"
                        )

                        .data(

                                bookService.getAllBooks(

                                        page,

                                        size,

                                        sortBy,

                                        direction
                                )
                        )

                        .build()
        );
    }

    // GET BOOKS BY CATEGORY

    @Operation(summary = "Get books by category")
    @GetMapping("/category")
    public ResponseEntity<
            ApiResponse<List<BookResponse>>
            >
    getBooksByCategory(

            @RequestParam
            String category
    ) {

        return ResponseEntity.ok(

                ApiResponse
                        .<List<BookResponse>>
                                builder()

                        .success(true)

                        .message(
                                "Books fetched successfully"
                        )

                        .data(

                                bookService
                                        .getBooksByCategory(category)
                        )

                        .build()
        );
    }

    // SEARCH BOOKS

    @Operation(summary = "Search books by title")
    @GetMapping("/search")
    public ResponseEntity<
            ApiResponse<List<BookResponse>>
            >
    searchBooks(

            @RequestParam
            String keyword
    ) {

        return ResponseEntity.ok(

                ApiResponse
                        .<List<BookResponse>>
                                builder()

                        .success(true)

                        .message(
                                "Books fetched successfully"
                        )

                        .data(

                                bookService
                                        .searchBooks(keyword)
                        )

                        .build()
        );
    }

    // FILTER BOOKS

    @Operation(summary = "Filter books")
    @GetMapping("/filter")
    public ResponseEntity<
            ApiResponse<List<BookResponse>>
            >
    filterBooks(

            @RequestParam
            String category,

            @RequestParam
            Double minPrice,

            @RequestParam
            Double maxPrice
    ) {

        return ResponseEntity.ok(

                ApiResponse
                        .<List<BookResponse>>
                                builder()

                        .success(true)

                        .message(
                                "Filtered Books Fetched Successfully"
                        )

                        .data(

                                bookService.filterBooks(

                                        category,

                                        minPrice,

                                        maxPrice
                                )
                        )

                        .build()
        );
    }

    // GET BOOK BY ID

    @Operation(summary = "Get book by ID")
    @GetMapping("/{id}")
    public ResponseEntity<
            ApiResponse<BookResponse>
            >
    getBookById(

            @PathVariable
            Long id
    ) {

        return ResponseEntity.ok(

                ApiResponse
                        .<BookResponse>
                                builder()

                        .success(true)

                        .message(
                                "Book fetched successfully"
                        )

                        .data(

                                bookService
                                        .getBookById(id)
                        )

                        .build()
        );
    }

    // UPDATE BOOK

    @Operation(summary = "Update existing book")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>>
    updateBook(

            @PathVariable
            Long id,

            @Valid
            @RequestBody
            BookRequest request
    ) {

        return ResponseEntity.ok(

                ApiResponse.builder()

                        .success(true)

                        .message(

                                bookService
                                        .updateBook(id, request)
                        )

                        .data(null)

                        .build()
        );
    }

    // DELETE BOOK

    @Operation(summary = "Delete book by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>>
    deleteBook(

            @PathVariable
            Long id
    ) {

        return ResponseEntity.ok(

                ApiResponse.builder()

                        .success(true)

                        .message(

                                bookService
                                        .deleteBook(id)
                        )

                        .data(null)

                        .build()
        );
    }
}