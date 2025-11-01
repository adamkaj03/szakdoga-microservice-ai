package com.adam.buzas.webshop.main.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonStringToObjectConverter {
    public Object convertJsonStringToObject(String jsonString, Class<?> targetClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, targetClass);
    }
}
