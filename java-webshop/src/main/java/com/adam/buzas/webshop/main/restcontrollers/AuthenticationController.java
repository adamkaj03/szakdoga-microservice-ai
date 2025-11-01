package com.adam.buzas.webshop.main.restcontrollers;

import com.adam.buzas.webshop.main.config.AuthenticationRequest;
import com.adam.buzas.webshop.main.config.RegisterRequest;
import com.adam.buzas.webshop.main.services.AuthenticationService;
import com.adam.buzas.webshop.main.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "https://purple-river-0f0577f03.4.azurestaticapps.net"})
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody RegisterRequest request){
        if(userService.isUserWithThisEmail(request.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This email is already exist!");
        }
        if(userService.isUserWithThisUsername(request.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This username is already exist!");
        }
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate (@RequestBody AuthenticationRequest request){
        if(!userService.isUserWithThisUsername(request.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no user with this username!");
        }
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody String token){
        authenticationService.logout(token);
        return ResponseEntity.ok("Logout was succesful!");
    }
}
