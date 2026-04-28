package com.derekgelvez.lawfirmweb.service;

import com.derekgelvez.lawfirmauth.model.Users;
import com.derekgelvez.lawfirmauth.repository.UserRepository;
import com.derekgelvez.lawfirmclient.dto.ClientResponse;
import com.derekgelvez.lawfirmclient.model.Client;
import com.derekgelvez.lawfirmcommon.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientEnricher {

    private final UserRepository userRepository;

    // Takes one raw Client and combines it with Users data
    public ClientResponse enrich(Client client) {
        Users user = userRepository.findById(client.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found for client: " + client.getId()));

        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setUserId(client.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(client.getPhone());
        response.setAddress(client.getAddress());
        return response;
    }

    // Takes a list of raw Clients and enriches all of them
    public List<ClientResponse> enrichAll(List<Client> clients) {
        return clients.stream()
                .map(this::enrich)
                .collect(Collectors.toList());
    }
}