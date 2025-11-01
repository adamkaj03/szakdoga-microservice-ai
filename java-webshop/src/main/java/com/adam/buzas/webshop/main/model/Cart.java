package com.adam.buzas.webshop.main.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * A webshopban a kosár entitást reprezentálja.
 */

@Data
public class Cart {
    private List<Book> cartContent = new ArrayList<>();
    private int amount;
}
