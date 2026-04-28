package com.derekgelvez.lawfirmweb.controller;

import com.derekgelvez.lawfirmauth.model.Users;
import com.derekgelvez.lawfirmclient.dto.ClientResponse;
import com.derekgelvez.lawfirmclient.dto.UpdateClientRequest;
import com.derekgelvez.lawfirmclient.model.Client;
import com.derekgelvez.lawfirmclient.service.ClientService;
import com.derekgelvez.lawfirmcommon.dto.ApiResponseDTO;
import com.derekgelvez.lawfirmweb.service.ClientEnricher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientEnricher clientEnricher;

    // Returns all clients — attorneys and clerks only
    @GetMapping
    public ApiResponseDTO<List<ClientResponse>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ApiResponseDTO.success("Clients retrieved successfully",
                clientEnricher.enrichAll(clients));
    }

    // Returns one client by their profile id — attorneys and clerks only
    @GetMapping("/{id}")
    public ApiResponseDTO<ClientResponse> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        return ApiResponseDTO.success("Client retrieved successfully",
                clientEnricher.enrich(client));
    }

    // Returns the logged in client's own profile
    @GetMapping("/me")
    public ApiResponseDTO<ClientResponse> getMyProfile() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        Users loggedInUser = (Users) authentication.getPrincipal();
        Client client = clientService.getClientByUserId(loggedInUser.getId());
        return ApiResponseDTO.success("Profile retrieved successfully",
                clientEnricher.enrich(client));
    }

    // Updates the logged in client's phone and address
    @PutMapping("/me")
    public ApiResponseDTO<ClientResponse> updateMyProfile(
            @RequestBody UpdateClientRequest request) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        Users loggedInUser = (Users) authentication.getPrincipal();
        Client client = clientService.updateClient(loggedInUser.getId(), request);
        return ApiResponseDTO.success("Profile updated successfully",
                clientEnricher.enrich(client));
    }
}