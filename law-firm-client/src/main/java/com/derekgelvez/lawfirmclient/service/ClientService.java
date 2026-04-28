package com.derekgelvez.lawfirmclient.service;

import com.derekgelvez.lawfirmclient.dto.UpdateClientRequest;
import com.derekgelvez.lawfirmclient.model.Client;
import com.derekgelvez.lawfirmclient.repository.ClientRepository;
import com.derekgelvez.lawfirmcommon.event.ClientRegisteredEvent;
import com.derekgelvez.lawfirmcommon.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    // Creates a blank client profile when a CLIENT registers
    public Client createClientProfile(Long userId) {
        Client client = new Client();
        client.setUserId(userId);
        return clientRepository.save(client);
    }

    // Returns raw Client object — enrichment happens in law-firm-web
    public Client getClientByUserId(Long userId) {
        return clientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client profile not found for user: " + userId));
    }

    // Returns raw Client object by profile id
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client not found with id: " + id));
    }

    // Returns all raw Client objects
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Updates phone and address, returns raw Client object
    public Client updateClient(Long userId, UpdateClientRequest request) {
        Client client = clientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client profile not found for user: " + userId));

        client.setPhone(request.getPhone());
        client.setAddress(request.getAddress());

        return clientRepository.save(client);
    }

    @EventListener
    public void handleClientRegistered(ClientRegisteredEvent event) {
        createClientProfile(event.getUserId());
    }
}