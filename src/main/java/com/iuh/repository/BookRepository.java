package com.iuh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.iuh.entity.*;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByBookId(Long bookId);
}
