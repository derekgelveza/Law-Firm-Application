package com.derekgelvez.lawfirmauth.controller;

import com.derekgelvez.lawfirmauth.model.Users;
import com.derekgelvez.lawfirmauth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public Users register(@RequestBody Users user){
        return service.register(user);
    }

}
