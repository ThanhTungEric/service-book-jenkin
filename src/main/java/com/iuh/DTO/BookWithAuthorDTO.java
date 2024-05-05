package com.iuh.DTO;

import com.iuh.entity.Book;

public class BookWithAuthorDTO {
    private Book book;
    private ExternalAuthorDTO author;

    // Getters and Setters
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public ExternalAuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(ExternalAuthorDTO author) {
        this.author = author;
    }
}
