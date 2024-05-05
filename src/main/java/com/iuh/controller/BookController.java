package com.iuh.controller;

import com.iuh.Config.RestTemplateConfig;
import com.iuh.DTO.BookWithAuthorDTO;
import com.iuh.DTO.ExternalAuthorDTO;
import com.iuh.entity.Book;
import com.iuh.service.BookService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/book")
public class BookController {
    private final BookService bookService;
    private final RestTemplateConfig restTemplateConfig;

    private static final String BASE_URL
            = "http://localhost:8080/api/author/";

    private static final String SERVICE_BOOK = "serviceBook";

    @Autowired
    public BookController(BookService bookService, RestTemplateConfig restTemplateConfig) {
        this.bookService = bookService;
        this.restTemplateConfig = restTemplateConfig;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBook();
    }

    @GetMapping("/{bookId}")
    @Cacheable(value = "book",key = "#bookId")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {
        Optional<Book> book = bookService.getBookById(bookId);
        return book.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping
    public Book saveBook(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @PutMapping("/{bookId}")
    @CachePut(value = "book",key = "#bookId")
    public ResponseEntity<Book> updateBook(@PathVariable Long bookId, @RequestBody Book bookUpdate) {
        try {
            Book updatedBook = bookService.updateBook(bookId, bookUpdate);
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{bookId}")
    @CacheEvict(value = "book", allEntries = true)
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        try {
            bookService.deleteBook(bookId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //rest template
    @Autowired
    private RestTemplateConfig restTemplate;

    //    RestTemplate

    @GetMapping("/author")
    @RateLimiter(name = SERVICE_BOOK)
    @CircuitBreaker(name = "randomActivity", fallbackMethod = "fallbackRandomActivity")
    public ResponseEntity<Object> getBookAndAuthor() {
        Optional<Book> bookOptional = bookService.getBookById(1L);

        if (bookOptional.isPresent()) {
            String authorUrl = BASE_URL + "1";
            Object author = restTemplate.getForObject(authorUrl, Object.class);

            Map<String, Object> result = new HashMap<>();
            result.put("book", bookOptional.get());
            result.put("author", author);

            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    public String fallbackRandomActivity(Throwable throwable) {
        return "Watch a video from TechPrimers";
    }
}
