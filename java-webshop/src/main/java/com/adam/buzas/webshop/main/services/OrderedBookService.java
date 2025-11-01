package com.adam.buzas.webshop.main.services;

import com.adam.buzas.webshop.main.model.Book;
import com.adam.buzas.webshop.main.model.Order;
import com.adam.buzas.webshop.main.model.OrderedBook;
import com.adam.buzas.webshop.main.repository.OrderedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderedBookService {

    @Autowired
    private OrderedBookRepository orderedBookRepository;

    public Optional<OrderedBook> getOrderedBook(int id){
        return orderedBookRepository.findById(id);
    }

    public void newOrderedBook(Order order, Book book, int count){
        OrderedBook orderedBook = new OrderedBook(order, book, count);
        orderedBookRepository.save(orderedBook);
    }
}
