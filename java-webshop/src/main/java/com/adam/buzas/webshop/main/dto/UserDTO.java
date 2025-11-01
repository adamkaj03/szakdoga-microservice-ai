package com.adam.buzas.webshop.main.dto;

import com.adam.buzas.webshop.main.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String name;
    private String userName;
    private String email;
    private Role role;

}
