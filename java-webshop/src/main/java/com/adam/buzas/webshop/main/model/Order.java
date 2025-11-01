package com.adam.buzas.webshop.main.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A rendeléseket reprezentáló entitás.
 */

@Entity
@Data
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "shipping_type")
    private ShippingType shippingType;


    @OneToMany(mappedBy="order", fetch = FetchType.LAZY)
    private List<OrderedBook> orderedBooks;

    private int price;


    public Order(LocalDateTime dateTime, String deliveryAddress, User user, ShippingType shippingType, int price) {
        this.dateTime = dateTime;
        this.deliveryAddress = deliveryAddress;
        this.user = user;
        this.shippingType = shippingType;
        this.price = price;
    }

}
