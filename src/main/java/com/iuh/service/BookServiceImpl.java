package com.iuh.service;

import com.iuh.entity.Book;
import com.iuh.repository.BookRepository;
import com.iuh.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBook() {
       return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(Long bookId) {

        return bookRepository.findByBookId(bookId);
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long bookId, Book bookUpdate) {
        Book book = bookRepository.findByBookId(bookId)
                .orElseThrow(() -> new RuntimeException("BOok not found with id: " + bookId));

        book.setIsbn(bookUpdate.getIsbn());
        book.setTitle(bookUpdate.getTitle());

        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findByBookId(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        bookRepository.delete(book);
    }
}
