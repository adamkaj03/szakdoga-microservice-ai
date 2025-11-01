package com.adam.buzas.webshop.main.services;

import com.adam.buzas.webshop.main.model.User;
import com.adam.buzas.webshop.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public User getUser(int id){
        return userRepository.findById(id).get();
    }
    public String getUsername(String nev){
        if(!userRepository.findByUsername(nev).isEmpty()){
            return userRepository.findByUsername(nev).get().getUsername();
        }
        else{
            return "";
        }

    }
    public String getUserEmail(String email){
        if(!userRepository.findByEmail(email).isEmpty()){
            return userRepository.findByEmail(email).get().getEmail();
        }
        else {
            return "";
        }
    }

    public boolean isUserWithThisEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isUserWithThisUsername(String username){
        return userRepository.findByUsername(username).isPresent();
    }

    public User newUser(User f){
        return userRepository.save(f);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).get();

        if (user == null) {
            throw new UsernameNotFoundException("Érvénytelen felhasználónév vagy jelszó");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().toString())
                .build();
    }

    public User getUserByUsername(String fnev) {
        return userRepository.findByUsername(fnev).get();
    }

    public List<User> getAllUser() {
        List<User> list = new ArrayList<>();
        for(User user : userRepository.findAll()){
            list.add(user);
        }
        return list;
    }
    public void deleteUser(int id){
        userRepository.deleteById(id);
    }
}
