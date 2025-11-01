package com.adam.buzas.webshop.main.restcontrollers;

import com.adam.buzas.webshop.main.model.ShippingType;
import com.adam.buzas.webshop.main.services.ShippingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200", "https://purple-river-0f0577f03.4.azurestaticapps.net"})
public class ShippingTypeController {

    @Autowired
    ShippingTypeService shippingTypeService;

    @GetMapping("/szallitasi_tipus")
    public List<ShippingType> getAllShippingType(){
        return shippingTypeService.getAllTypes();
    }

    @GetMapping("/szallitasi_tipus/{id}")
    public Optional<ShippingType> getBookById(@PathVariable("id") int id){
        return shippingTypeService.getTypeById(id);
    }
}
