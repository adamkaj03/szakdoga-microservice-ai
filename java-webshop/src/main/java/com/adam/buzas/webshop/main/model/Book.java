package com.adam.buzas.webshop.main.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A webshopban megvásárolható könyv entitásokat reprezentálja.
 */
@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String author;

    @Column(name = "publish_year")
    private int publishYear;

    private int price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(length = 10000)
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Book(String title, String author, int publishYear, int price, Category category, String imgUrl, String description, LocalDateTime createdAt) {
        this.title = title;
        this.author = author;
        this.publishYear = publishYear;
        this.price = price;
        this.category = category;
        this.imgUrl = imgUrl;
        this.description = description;
        this.createdAt = createdAt;
    }

}
