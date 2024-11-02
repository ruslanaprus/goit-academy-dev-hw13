package org.example.service;

import org.example.config.HibernateConfig;
import org.example.dto.TicketDTO;
import org.example.model.Client;
import org.example.model.Planet;
import org.example.model.Ticket;
import org.hibernate.Session;

import java.sql.Timestamp;
import java.util.List;

public class TicketService extends GenericService<Ticket, Long> {
    private static TicketService instance;

    public TicketService() {
        super(Ticket.class);
    }

    public static TicketService getInstance() {
        if (instance == null) {
            instance = new TicketService();
        }
        return instance;
    }

    public List<TicketDTO> findAllTicketDetails() {
        try (Session session = HibernateConfig.getInstance().getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT new org.example.dto.TicketDTO(t.id, t.createdAt, c.id, c.name, fp.id, fp.name, tp.id, tp.name) " +
                                    "FROM Ticket t JOIN t.client c JOIN t.fromPlanet fp JOIN t.toPlanet tp", TicketDTO.class)
                    .getResultList();
        }
    }

    public List<Ticket> findAllWithDetails() {
        try (Session session = HibernateConfig.getInstance().getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT t FROM Ticket t JOIN FETCH t.client JOIN FETCH t.fromPlanet JOIN FETCH t.toPlanet", Ticket.class)
                    .getResultList();
        }
    }

    public Ticket createTicketWithIds(Long clientId, String fromPlanetId, String toPlanetId) {
        Client client = new ClientService().findById(clientId);
        Planet fromPlanet = new PlanetService().findById(fromPlanetId);
        Planet toPlanet = new PlanetService().findById(toPlanetId);

        if (client == null || fromPlanet == null || toPlanet == null) {
            throw new IllegalArgumentException("Invalid IDs provided for client or planets.");
        }

        Ticket ticket = new Ticket();
        ticket.setClient(client);
        ticket.setFromPlanet(fromPlanet);
        ticket.setToPlanet(toPlanet);
        ticket.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        save(ticket);
        return ticket;
    }
}