package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Client;
import org.thymeleaf.context.Context;

import org.example.config.TemplateConfig;
import org.example.service.ClientService;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {
        "/",
        "/clientManager",
        "/createClientForm",
        "/findClientByIdForm",
        "/updateClientForm",
        "/deleteClientByIdForm",
        "/findById",
        "/findAllClients",
        "/createClient",
        "/updateClient",
        "/deleteClientById"})
public class TravelServlet extends HttpServlet {
    private final TemplateConfig templateConfig = new TemplateConfig();
    private final ClientService clientService = new ClientService();

    public TravelServlet() throws ServletException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String servletPath = req.getServletPath();
        Context context = new Context();
        String message = req.getParameter("message");
        if (message != null) {
            context.setVariable("message", message);
        }
        switch (servletPath) {
            case "/" -> templateConfig.process("home", context, res);
            case "/clientManager" -> templateConfig.process("client", context, res);
            case "/createClientForm" -> {
                context.setVariable("action", "createClientForm");
                templateConfig.process("client", context, res);
            }
            case "/findClientByIdForm" -> {
                context.setVariable("action", "findByIdForm");
                templateConfig.process("client", context, res);
            }
            case "/findById" -> {
                String clientId = req.getParameter("clientId");
                Client clientById = clientService.findById(Long.parseLong(clientId));
                if (clientById != null) {
                    context.setVariable("client", clientById);
                    context.setVariable("message", "Client found successfully.");
                } else {
                    context.setVariable("message", "Client not found.");
                }
                context.setVariable("action", "clientDetails");
                templateConfig.process("client", context, res);
            }
            case "/findAllClients" -> {
                List<Client> clients = clientService.findAll();
                context.setVariable("action", "allClientDetails");
                context.setVariable("clients", clients);
                templateConfig.process("client", context, res);
            }
            case "/updateClientForm" -> {
                context.setVariable("action", "updateClientForm");
                templateConfig.process("client", context, res);
            }
            case "/updateClient" -> {
                String clientId = req.getParameter("clientId");
                String name = req.getParameter("name");
                String email = req.getParameter("email");
                Client updatedClient = new Client();
                boolean isUpdated = false;

                if (name != null && !name.isEmpty()) {
                    updatedClient.setName(name);
                }
                if (email != null && !email.isEmpty()) {
                    updatedClient.setEmail(email);
                }

                try {
                    isUpdated = clientService.updateClient(Long.parseLong(clientId), updatedClient);
                    context.setVariable("message", isUpdated ? "Client updated successfully." : "Failed to update client.");
                } catch (Exception e) {
                    context.setVariable("message", "Error updating client: " + e.getMessage());
                }

                context.setVariable("action", "updateClient");
                templateConfig.process("client", context, res);
            }
            case "/deleteClientByIdForm" -> {
                context.setVariable("action", "deleteClientByIdForm");
                templateConfig.process("client", context, res);
            }
            case "/deleteClientById" -> {
                String clientId = req.getParameter("clientId");
                boolean isDeleted = clientService.deleteClient(Long.parseLong(clientId));
                context.setVariable("message", isDeleted ? "Client deleted successfully." : "Failed to delete client.");
                context.setVariable("action", "deleteClient");
                templateConfig.process("client", context, res);
            }
            default -> templateConfig.process("client", context, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Context context = new Context();
        try {
            clientService.saveClient(req.getReader());
            context.setVariable("message", "Client created successfully.");
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            context.setVariable("message", "Error creating client: " + e.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        context.setVariable("action", "createClient");
        templateConfig.process("client", context, res);
    }
}