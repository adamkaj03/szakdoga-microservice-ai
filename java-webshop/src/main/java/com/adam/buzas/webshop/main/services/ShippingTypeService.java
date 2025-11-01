package com.adam.buzas.webshop.main.services;

import com.adam.buzas.webshop.main.model.ShippingType;
import com.adam.buzas.webshop.main.repository.ShippingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShippingTypeService {

    @Autowired
    ShippingTypeRepository shippingTypeRepository;

    public List<ShippingType> getAllTypes() {
        List<ShippingType> list = new ArrayList<>();
        for(ShippingType k : shippingTypeRepository.findAll()){
            list.add(k);
        }
        return list;
    }

    public Optional<ShippingType> getTypeById(int id) {
        return shippingTypeRepository.findById(id);
    }
}
