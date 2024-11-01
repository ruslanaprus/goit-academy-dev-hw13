package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.GenericDao;
import org.example.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    private final GenericDao<Client, Long> clientDao = new GenericDao<>(Client.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void saveClient(BufferedReader payload) throws IOException {
        Client client = objectMapper.readValue(payload, Client.class);
        logger.info("Saving client from payload: {}", client);
        clientDao.save(client);
        logger.info("Client saved successfully from payload");
    }

    public Client findById(Long id) {
        logger.info("Finding client with ID: {}", id);
        return clientDao.findById(id);
    }

    public List<Client> findAll() {
        return clientDao.findAll();
    }

    public boolean updateClient(Long id, Client client) {
        try {
            int rowsUpdated = clientDao.update(id, client);
            return rowsUpdated > 0;
        } catch (Exception e) {
            logger.error("Failed to update client with id: {}", id, e);
            return false;
        }
    }

    public boolean deleteClient(Long id) {
        try {
            logger.info("Deleting client with id: {}", id);
            int rowsDeleted = clientDao.delete(id);
            if (rowsDeleted > 0) {
                logger.info("Client deleted successfully");
                return true;
            } else {
                logger.warn("No client found with id: {}", id);
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed to delete client with id: {}", id, e);
            return false;
        }
    }

}