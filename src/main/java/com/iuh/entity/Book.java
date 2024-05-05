package com.iuh.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Book {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long bookId;
    private String title;
    private String isbn;

    private Long authorId;
    private String name;
    private String address;
}
