package com.iuh.service;

import com.iuh.entity.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAllBook();
    Optional<Book> getBookById(Long bookId);
    Book saveBook(Book book);
    Book updateBook(Long bookId, Book bookUpdate);
    void deleteBook(Long bookId);
}
