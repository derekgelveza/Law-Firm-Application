package com.derekgelvez.lawfirmclient.service;


import com.derekgelvez.lawfirmauth.model.Users;
import com.derekgelvez.lawfirmclient.dto.UpdateClientRequest;
import com.derekgelvez.lawfirmclient.model.Client;
import com.derekgelvez.lawfirmcommon.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.derekgelvez.lawfirmauth.repository.UserRepository;
import com.derekgelvez.lawfirmclient.repository.ClientRepository;
import com.derekgelvez.lawfirmclient.dto.ClientResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public Client createClientProfile(Long userId) {
        Client client = new Client();
        client.setUserId(userId);
        return clientRepository.save(client);
    }

    //for client trying to log in
    public ClientResponse getClientByUserId(Long userId){
        Client client = clientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client profile not foud for user: " + userId
                ));
        return buildResponse(client);
    }

    //for attorney or clerk looking up a client by id
    public ClientResponse getClientById(Long id){
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client not found with id:" + id
                ));
        return buildResponse(client);
    }

    //allows attorneys and clkers to see all clients
    public List<ClientResponse> getAllClients(){
        return clientRepository.findAll()
                .stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    public ClientResponse updateClient(Long userId, UpdateClientRequest request) {
        Client client = clientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client profile not found for user: " + userId));

        client.setPhone(request.getPhone());
        client.setAddress(request.getAddress());

        return buildResponse(clientRepository.save(client));
    }

    //builds response
    private ClientResponse buildResponse(Client client) {
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
}
