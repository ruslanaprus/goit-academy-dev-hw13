package org.example.service;

import org.example.model.Planet;

public class PlanetService extends GenericService<Planet, String>{
    public PlanetService() {
        super(Planet.class);
    }
}