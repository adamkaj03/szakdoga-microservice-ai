package com.adam.buzas.webshop.main.services;


import com.adam.buzas.webshop.main.dto.EmailDto;
import com.adam.buzas.webshop.main.model.*;
import com.adam.buzas.webshop.main.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
    @Autowired
    EmailService emailService;

    public Optional<Order> getOrder(Order order){
        return orderRepository.findById(order.getId());
    }
    public Optional<Order> getOrder(int id){
        return orderRepository.findById(id);
    }


    public Order newOrder(LocalDateTime date, String deliveryAddress, String username, Cart cart, ShippingType shippingType){
        int fullPrice = cart.getAmount() + shippingType.getPrice();

        User user = userService.getUserByUsername(username);
        Order order = new Order(date, deliveryAddress, user, shippingType, fullPrice);
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
        createOrderConfirmationEmail(user, order);
        return order;
    }

    /**
     * Létrehozza és elküldi a rendelés visszaigazoló emailt
     */
    private void createOrderConfirmationEmail(User user, Order order) {
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(user.getEmail());
        emailDto.setSubject("Order Confirmation - Order #" + order.getId());
        emailDto.setBody(emailService.loadEmailTemplate("order-confirmation.txt"));
        emailService.sendEmail(emailDto);
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
