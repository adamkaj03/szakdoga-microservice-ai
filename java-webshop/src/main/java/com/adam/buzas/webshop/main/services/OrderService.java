package com.adam.buzas.webshop.main.services;


import com.adam.buzas.webshop.main.repository.OrderRepository;
import com.adam.buzas.webshop.main.model.Book;
import com.adam.buzas.webshop.main.model.Cart;
import com.adam.buzas.webshop.main.model.Order;
import com.adam.buzas.webshop.main.model.ShippingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserService userService;
    @Autowired
    OrderedBookService orderedBookService;
    @Autowired
    BookService bookService;
    @Autowired
    ShippingTypeService shippingTypeService;

    public Optional<Order> getOrder(Order order){
        return orderRepository.findById(order.getId());
    }
    public Optional<Order> getOrder(int id){
        return orderRepository.findById(id);
    }


    public Order newOrder(LocalDateTime date, String deliveryAddress, String username, Cart cart, ShippingType shippingType){
        int fullPrice = cart.getAmount() + shippingType.getPrice();

        Order order = new Order(date, deliveryAddress, userService.getUserByUsername(username), shippingType, fullPrice);
        orderRepository.save(order);
        Map<Integer, Integer> bookIdCount = new HashMap<>();
        for(Book book : cart.getCartContent()){
            int count = bookIdCount.getOrDefault(book.getId(), 0);
            bookIdCount.put(book.getId(), count+1);
        }

        order = getOrder(order).get();
        for (Map.Entry<Integer, Integer> entry : bookIdCount.entrySet()) {
            orderedBookService.newOrderedBook(order,
                    bookService.getBookById(entry.getKey()).get(),
                    entry.getValue());
        }
        return order;
    }

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        for(Order order : orderRepository.findAll()){
            list.add(order);
        }
        return list;
    }

    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }



}
