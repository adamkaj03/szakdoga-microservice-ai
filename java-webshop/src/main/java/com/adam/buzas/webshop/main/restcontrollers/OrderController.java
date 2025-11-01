package com.adam.buzas.webshop.main.restcontrollers;

import com.adam.buzas.webshop.main.dto.OrderDTO;
import com.adam.buzas.webshop.main.dto.OrderRequest;
import com.adam.buzas.webshop.main.model.Order;
import com.adam.buzas.webshop.main.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://purple-river-0f0577f03.4.azurestaticapps.net"})
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    Converter<Order, OrderDTO> converter;

    @GetMapping("/rendelesek")
    public List<OrderDTO> getAllOrders(){
        List<OrderDTO> list = new ArrayList<>();
        for (Order o : orderService.getAllOrders()){
            list.add(converter.convert(o));
        }
        return list;
    }

    @GetMapping("/rendelesek/{id}")
    public OrderDTO getOrderById(@PathVariable("id") int id){
        return converter.convert(orderService.getOrder(id).get());
    }

    @PostMapping("/rendelesek")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request){
        orderService.newOrder(LocalDateTime.now(), request.getAddress(), request.getUsername(), request.getCart(), request.getShippingType());
        return ResponseEntity.ok("Siker");
    }


    @DeleteMapping("/rendelesek/{id}")
    public void deleteOrder(@PathVariable("id") int id){
        orderService.deleteOrder(id);
    }
}
