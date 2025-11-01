package com.adam.buzas.webshop.main.repository;

import com.adam.buzas.webshop.main.model.OrderedBook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedBookRepository extends CrudRepository<OrderedBook, Integer> {
}
