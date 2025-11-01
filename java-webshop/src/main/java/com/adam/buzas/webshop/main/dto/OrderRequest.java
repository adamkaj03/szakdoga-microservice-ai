package com.adam.buzas.webshop.main.dto;

import com.adam.buzas.webshop.main.model.Cart;
import com.adam.buzas.webshop.main.model.ShippingType;
import lombok.Data;

@Data
public class OrderRequest {
    private String address;
    private String username;
    private Cart cart;
    private ShippingType shippingType;
}