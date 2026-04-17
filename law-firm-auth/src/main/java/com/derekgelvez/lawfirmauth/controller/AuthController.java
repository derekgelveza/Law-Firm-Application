package com.derekgelvez.lawfirmauth.controller;

import com.derekgelvez.lawfirmauth.dto.LoginRequest;
import com.derekgelvez.lawfirmauth.model.Users;
import com.derekgelvez.lawfirmauth.service.AuthService;
import com.derekgelvez.lawfirmcommon.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ApiResponseDTO<String> register(@RequestBody Users user) {
        service.register(user);
        return ApiResponseDTO.success("User registered successfully", null);
    }

    @PostMapping("/login")
    public ApiResponseDTO<String> login(@RequestBody LoginRequest request) {
        String token = service.verify(request);
        return ApiResponseDTO.success("Login successful", token);
    }
}