package org.example.service;

import org.example.model.Client;

public class ClientService extends GenericService<Client, Long> {
    public ClientService() {
        super(Client.class);
    }
}