package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.TicketDTO;
import org.example.model.Planet;
import org.example.model.Ticket;
import org.example.service.PlanetService;
import org.example.service.TicketService;
import org.example.config.TemplateConfig;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {
        "/ticketManager",
        "/createTicketForm",
        "/findTicketByIdForm",
        "/updateTicketForm",
        "/deleteTicketByIdForm",
        "/findTicketById",
        "/findAllTickets",
        "/createTicket",
        "/updateTicket",
        "/deleteTicketById"})
public class TicketServlet extends HttpServlet {
    private final TemplateConfig templateConfig = new TemplateConfig();
    private final TicketService ticketService = TicketService.getInstance();
    private final PlanetService planetService = new PlanetService();

    public TicketServlet() throws ServletException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        Context context = new Context();

        switch (servletPath) {
            case "/ticketManager" -> templateConfig.process("ticket", context, res);
            case "/createTicketForm" -> {
                context.setVariable("action", "createTicketForm");
                templateConfig.process("ticket", context, res);
            }
            case "/findTicketByIdForm" -> {
                context.setVariable("action", "findTicketByIdForm");
                templateConfig.process("ticket", context, res);
            }
            case "/findTicketById" -> {
                Long ticketId = Long.valueOf(req.getParameter("ticketsID"));
                TicketDTO ticketById = ticketService.findTicketDetailsById(ticketId);
                if (ticketById != null) {
                    context.setVariable("ticket", ticketById);
                    context.setVariable("message", "Ticket found successfully.");
                } else {
                    context.setVariable("message", "Ticket not found.");
                }
                context.setVariable("action", "ticketDetails");
                templateConfig.process("ticket", context, res);
            }
            case "/findAllTickets" -> {
                List<TicketDTO> tickets = ticketService.findAllTicketDetails();
                context.setVariable("tickets", tickets);
                context.setVariable("action", "allTicketDetails");
                templateConfig.process("ticket", context, res);
            }
            case "/updateTicketForm" -> {
                context.setVariable("action", "updateTicketForm");
                templateConfig.process("ticket", context, res);
            }
            case "/updateTicket" -> {
                Long ticketId = Long.valueOf(req.getParameter("ticketId"));
                String fromPlanetId = req.getParameter("fromPlanetId");
                String toPlanetId = req.getParameter("toPlanetId");

                boolean isUpdated = false;

                Ticket existingTicket = ticketService.findById(ticketId);
                if (existingTicket != null) {
                    if (fromPlanetId != null && !fromPlanetId.isEmpty()) {
                        Planet fromPlanet = planetService.findById(fromPlanetId);
                        if (fromPlanet != null) {
                            existingTicket.setFromPlanet(fromPlanet);
                        } else {
                            context.setVariable("message", "Invalid fromPlanetId provided.");
                            templateConfig.process("ticket", context, res);
                            return;
                        }
                    }

                    if (toPlanetId != null && !toPlanetId.isEmpty()) {
                        Planet toPlanet = planetService.findById(toPlanetId);
                        if (toPlanet != null) {
                            existingTicket.setToPlanet(toPlanet);
                        } else {
                            context.setVariable("message", "Invalid toPlanetId provided.");
                            templateConfig.process("ticket", context, res);
                            return;
                        }
                    }

                    isUpdated = ticketService.update(ticketId, existingTicket);
                    context.setVariable("message", isUpdated ? "Ticket updated successfully." : "Failed to update ticket.");
                } else {
                    context.setVariable("message", "Ticket not found.");
                }

                context.setVariable("action", "updateTicket");
                templateConfig.process("ticket", context, res);
            }

            case "/deleteTicketByIdForm" -> {
                context.setVariable("action", "deleteTicketByIdForm");
                templateConfig.process("ticket", context, res);
            }
            case "/deleteTicketById" -> {
                Long ticketId = Long.valueOf(req.getParameter("ticketId"));
                boolean isDeleted = ticketService.delete(ticketId);
                context.setVariable("message", isDeleted ? "Ticket deleted successfully." : "Failed to delete ticket.");
                context.setVariable("action", "deleteTicketById");
                templateConfig.process("ticket", context, res);
            }
            default -> templateConfig.process("ticket", context, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Context context = new Context();
        try {
            Long clientId = Long.valueOf(req.getParameter("clientId"));
            String fromPlanetId = req.getParameter("fromPlanetId");
            String toPlanetId = req.getParameter("toPlanetId");
            ticketService.createTicketWithIds(clientId, fromPlanetId, toPlanetId);
            context.setVariable("message", "Ticket created successfully.");
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            context.setVariable("message", "Error creating ticket: " + e.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        context.setVariable("action", "createTicket");
        templateConfig.process("ticket", context, res);
    }
}