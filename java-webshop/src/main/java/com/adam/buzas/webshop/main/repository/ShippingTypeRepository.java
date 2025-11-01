package com.adam.buzas.webshop.main.repository;

import com.adam.buzas.webshop.main.model.ShippingType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingTypeRepository extends CrudRepository<ShippingType, Integer> {
}
