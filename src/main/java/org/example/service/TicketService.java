package org.example.service;

import org.example.model.Ticket;

public class TicketService extends GenericService<Ticket, Long> {
    public TicketService() {
        super(Ticket.class);
    }
}
