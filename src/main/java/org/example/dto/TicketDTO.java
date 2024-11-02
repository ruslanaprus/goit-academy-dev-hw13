package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDTO {
    private Long id;
    private Timestamp createdAt;
    private Long clientId;
    private String clientName;
    private String fromPlanetId;
    private String fromPlanetName;
    private String toPlanetId;
    private String toPlanetName;
}
