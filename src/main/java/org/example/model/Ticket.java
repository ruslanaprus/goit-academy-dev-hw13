package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tickets_seq")
    @SequenceGenerator(name = "tickets_seq", sequenceName = "seq_tickets_id", allocationSize = 1)
    private Long id;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "client_id")
    private Long clientId;
    @Column(name = "from_planet_id")
    private String fromPlanetId;
    @Column(name = "to_planet_id")
    private String toPlanetId;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
}