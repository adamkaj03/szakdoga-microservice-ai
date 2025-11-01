package com.adam.buzas.webshop.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A rendelt könyveket reprezentáló entitás.
 */

@Entity
@Data
@Table(name = "ordered_book")
@NoArgsConstructor
public class OrderedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private int count;

    public OrderedBook(Order order, Book book, int count) {
        this.order = order;
        this.book = book;
        this.count = count;
    }
}
