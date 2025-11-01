package com.adam.buzas.webshop.main.converter;

import com.adam.buzas.webshop.main.dto.UserDTO;
import com.adam.buzas.webshop.main.model.User;
import org.springframework.core.convert.converter.Converter;


public class UserToDTOConverter implements Converter<User, UserDTO> {

    @Override
    public UserDTO convert(User source) {
        return new UserDTO(source.getId(), source.getName(), source.getUsername(), source.getEmail(), source.getRole());
    }
}
