package com.derekgelvez.lawfirmweb.service;

import com.derekgelvez.lawfirmauth.dto.InviteRegisterRequest;
import com.derekgelvez.lawfirmauth.model.Role;
import com.derekgelvez.lawfirmauth.model.Users;
import com.derekgelvez.lawfirmauth.service.AuthService;
import com.derekgelvez.lawfirmclient.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationOrchestrator {

    private final AuthService authService;
    private final ClientService clientService;

    public void register(InviteRegisterRequest request) {

        // Step 1 — create the user account
        Users savedUser = authService.registerWithInvite(request);

        // Step 2 — if the new user is a CLIENT, create their profile
        if (savedUser.getRole() == Role.CLIENT) {
            clientService.createClientProfile(savedUser.getId());
        }


    }
}