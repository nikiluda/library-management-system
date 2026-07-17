package com.zhanlin.library_management_system.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title should is not be empty")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Author should is not be empty")
    @Column(nullable = false)
    private String author;

    @NotNull(message = "Publication year should is not be empty")
    @Min(value = 1650, message = "Year should is between 1650 and 2026")
    @Max(value = 2026, message = "Year should is between 1650 and 2026")
    @Column(name = "publication_year", nullable = false)
    private Integer year;

    @NotBlank(message = "ISBN should is not be empty")
    @Pattern(regexp = "^(\\\\d{10}|\\\\d{13})$", message = "ISBN must be exactly 10 or 13 digits")
    @Column(nullable = false, unique = true)
    private String isbn;

    @NotNull(message = "Total copies count is required")
    @Min(value = 0 , message = "Total copies cannot be negative")
    @Column(name = "total_copies", nullable = false)
    private Integer totalCopies;


    @NotNull(message = "Available copies count is required")
    @Min(value = 0, message = "Available copies cannot be negative")
    @Column(name = "available_copies")
    private Integer availableCopies;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<BookLoan> loans;

    public Book() {

    }

    public Book(String title, String author,String isbn, Integer year, Integer totalCopies, Integer availableCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }
}
