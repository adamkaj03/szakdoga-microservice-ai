package com.adam.buzas.webshop.main.restcontrollers;

import com.adam.buzas.webshop.main.dto.UserDTO;
import com.adam.buzas.webshop.main.model.User;
import com.adam.buzas.webshop.main.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://purple-river-0f0577f03.4.azurestaticapps.net"})
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    Converter<User, UserDTO> converter;

    @GetMapping("/felhasznalok")
    public List<UserDTO> getAllUsers(){
        List<UserDTO> list = new ArrayList<>();
        for (User u : userService.getAllUser()){
            list.add(converter.convert(u));
        }
        return list;
    }

    @GetMapping("/felhasznalok/{id}")
    public UserDTO getUserById(@PathVariable("id") int id){
        User u = userService.getUser(id);
        return converter.convert(u);
    }

    @PostMapping("/felhasznalok")
    public User createUser(@RequestBody User user){
        return userService.newUser(user);
    }

    @PutMapping("/felhasznalok/{id}")
    public UserDTO updateUser(@PathVariable("id") int id,
                           @RequestBody User user) {
        user.setId(id);
        User u = userService.newUser(user);
        return converter.convert(u);
    }

    @DeleteMapping("/felhasznalok/{id}")
    public void deleteUser(@PathVariable("id") int id){
        userService.deleteUser(id);
    }
}
