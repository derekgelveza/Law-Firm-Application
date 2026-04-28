package com.derekgelvez.lawfirmauth.controller;

import com.derekgelvez.lawfirmauth.dto.ChangePasswordRequest;
import com.derekgelvez.lawfirmauth.dto.CreateInviteRequest;
import com.derekgelvez.lawfirmauth.dto.InviteRegisterRequest;
import com.derekgelvez.lawfirmauth.dto.LoginRequest;
import com.derekgelvez.lawfirmauth.model.Invitation;
import com.derekgelvez.lawfirmauth.model.Users;
import com.derekgelvez.lawfirmauth.service.AuthService;
import com.derekgelvez.lawfirmauth.service.InvitationService;
import com.derekgelvez.lawfirmcommon.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final InvitationService invitationService;

    @PostMapping("/login")
    public ApiResponseDTO<String> login(@RequestBody LoginRequest request) {
        String token = authService.verify(request);
        return ApiResponseDTO.success("Login successful", token);
    }

    @PostMapping("/register")
    public ApiResponseDTO<String> register(
            @RequestBody InviteRegisterRequest request) {
        authService.registerWithInvite(request);
        return ApiResponseDTO.success("Registration successful", null);
    }

    @PostMapping("/invite")
    public ApiResponseDTO<String> createInvite(@RequestBody CreateInviteRequest request) {
        String createdByEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Invitation invitation = invitationService.createInviteRequest(request, createdByEmail);


        return ApiResponseDTO.success("Invitation created successfully", invitation.getToken());
    }

    @PutMapping("/change-password")
    public ApiResponseDTO<String> changePassword(
            @RequestBody ChangePasswordRequest request) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        authService.changePassword(request, email);
        return ApiResponseDTO.success("Password changed successfully", null);
    }
}