package com.derekgelvez.lawfirmclient.controller;


import com.derekgelvez.lawfirmauth.model.Users;
import com.derekgelvez.lawfirmclient.dto.ClientResponse;
import com.derekgelvez.lawfirmclient.dto.UpdateClientRequest;
import com.derekgelvez.lawfirmclient.service.ClientService;
import com.derekgelvez.lawfirmcommon.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;


    //returns all clients. Only accessible by attorneys and clerks
    @GetMapping
    public ApiResponseDTO<List<ClientResponse>> getAllClients(){
        List<ClientResponse> clients = clientService.getAllClients();
        return ApiResponseDTO.success("Clients retrieved successfully", clients);
    }

    //returns client by their id
    @GetMapping("/{id}")
    public ApiResponseDTO<ClientResponse> getClientById(@PathVariable Long id){
        ClientResponse client = clientService.getClientById(id);
        return ApiResponseDTO.success("Client retrieved successfully", client);
    }


    //returns te logged in client's profile
    //returned from JWT token
    @GetMapping("/me")
    public ApiResponseDTO<ClientResponse> getMyProfile(){
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        Users loggedInUser = (Users) authentication.getPrincipal();

        ClientResponse client = clientService.getClientByUserId(
                loggedInUser.getId());

        return ApiResponseDTO.success("Profile retrieved successfully", client);
    }

    @PutMapping("/me")
    public ApiResponseDTO<ClientResponse> updateMyProfile(@RequestBody UpdateClientRequest request){

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        Users loggedInUser = (Users) authentication.getPrincipal();

        ClientResponse updated = clientService.updateClient(
                loggedInUser.getId(), request);

        return ApiResponseDTO.success("Profile updated successfully", updated);
    }


}
