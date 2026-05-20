package org.example.bookcart.service;

import lombok.RequiredArgsConstructor;
import org.example.bookcart.dto.BookRequest;
import org.example.bookcart.dto.BookResponse;
import org.example.bookcart.entity.Book;
import org.example.bookcart.exception.ResourceNotFoundException;
import org.example.bookcart.repository.BookRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // ADD BOOK

    public String addBook(
            BookRequest request
    ) {

        Book book = new Book();

        book.setTitle(
                request.getTitle()
        );

        book.setAuthor(
                request.getAuthor()
        );

        book.setCategory(
                request.getCategory()
        );

        book.setImageUrl(
                request.getImageUrl()
        );

        book.setPrice(
                request.getPrice()
        );

        book.setStock(
                request.getStock()
        );

        bookRepository.save(book);

        return "Book Added Successfully";
    }

    // ENTITY TO DTO

    private BookResponse mapToResponse(
            Book book
    ) {

        return BookResponse.builder()

                .id(book.getId())

                .title(book.getTitle())

                .author(book.getAuthor())

                .category(book.getCategory())

                .imageUrl(book.getImageUrl())

                .price(book.getPrice())

                .stock(book.getStock())

                .build();
    }

    // GET ALL BOOKS

    public Page<BookResponse> getAllBooks(

            int page,

            int size,

            String sortBy,

            String direction
    ) {

        Sort sort =

                direction.equalsIgnoreCase("asc")

                        ? Sort.by(sortBy).ascending()

                        : Sort.by(sortBy).descending();

        Pageable pageable =

                PageRequest.of(
                        page,
                        size,
                        sort
                );

        Page<Book> books =
                bookRepository.findAll(pageable);

        List<BookResponse> responses =

                books.getContent()

                        .stream()

                        .map(this::mapToResponse)

                        .toList();

        return new PageImpl<>(

                responses,

                pageable,

                books.getTotalElements()
        );
    }

    // SEARCH CATEGORY

    public List<BookResponse>
    getBooksByCategory(
            String category
    ) {

        return bookRepository

                .findByCategory(category)

                .stream()

                .map(this::mapToResponse)

                .toList();
    }

    // SEARCH TITLE

    public List<BookResponse>
    searchBooks(
            String keyword
    ) {

        return bookRepository

                .findByTitleContainingIgnoreCase(keyword)

                .stream()

                .map(this::mapToResponse)

                .toList();
    }

    // FILTER BOOKS

    public List<BookResponse>
    filterBooks(

            String category,

            Double minPrice,

            Double maxPrice
    ) {

        return bookRepository

                .findByCategoryAndPriceBetween(

                        category,

                        minPrice,

                        maxPrice
                )

                .stream()

                .map(this::mapToResponse)

                .toList();
    }

    // GET BOOK BY ID

    public BookResponse getBookById(
            Long id
    ) {

        Book book =

                bookRepository.findById(id)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Book Not Found"
                                )
                        );

        return mapToResponse(book);
    }

    // UPDATE BOOK

    public String updateBook(

            Long id,

            BookRequest request
    ) {

        Book book =

                bookRepository.findById(id)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Book Not Found"
                                )
                        );

        book.setTitle(
                request.getTitle()
        );

        book.setAuthor(
                request.getAuthor()
        );

        book.setCategory(
                request.getCategory()
        );

        book.setImageUrl(
                request.getImageUrl()
        );

        book.setPrice(
                request.getPrice()
        );

        // ADD STOCK

        book.setStock(

                book.getStock()

                        + request.getStock()
        );

        bookRepository.save(book);

        return "Book Updated Successfully";
    }

    // DELETE BOOK

    public String deleteBook(
            Long id
    ) {

        Book book =

                bookRepository.findById(id)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Book Not Found"
                                )
                        );

        bookRepository.delete(book);

        return "Book Deleted Successfully";
    }

    // LOW STOCK COUNT

    public Long getLowStockBooksCount() {

        return bookRepository
                .getLowStockBooksCount();
    }

    // MOST SOLD BOOK

    public String getMostSoldBook() {

        String book =

                bookRepository
                        .getMostSoldBook();

        return book != null
                ? book
                : "No Sales Yet";
    }
}