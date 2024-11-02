package org.example.service;

import org.example.config.HibernateConfig;
import org.example.dto.TicketDTO;
import org.example.model.Client;
import org.example.model.Planet;
import org.example.model.Ticket;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketService extends GenericService<Ticket, Long> {
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

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
        ticket.setCreatedAt(Timestamp.from(Instant.now()));

        save(ticket);
        return ticket;
    }

    public TicketDTO findTicketDetailsById(Long ticketId) {
        String hql = "SELECT new org.example.dto.TicketDTO(t.id, t.createdAt, c.id, c.name, fp.id, fp.name, tp.id, tp.name) " +
                "FROM Ticket t JOIN t.client c JOIN t.fromPlanet fp JOIN t.toPlanet tp WHERE t.id = :ticketId";

        Map<String, Object> params = new HashMap<>();
        params.put("ticketId", ticketId);

        List<TicketDTO> results = findWithQuery(hql, TicketDTO.class, params);
        if (results.isEmpty()) {
            return null;
        }
        logger.info("ticket found {}", results);
        return results.get(0);
    }

}