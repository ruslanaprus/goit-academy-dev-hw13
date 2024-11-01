package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.GenericDao;
import org.example.model.Planet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class PlanetService {
    private static final Logger logger = LoggerFactory.getLogger(PlanetService.class);
    private final GenericDao<Planet, String> planetDao = new GenericDao<>(Planet.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void save(Planet planet) {
        logger.info("Saving planet: {}", planet);
        planetDao.save(planet);
    }

    public void savePlanet(BufferedReader payload) throws IOException {
        Planet planet = objectMapper.readValue(payload, Planet.class);
        logger.info("Saving planet from payload: {}", planet);
        planetDao.save(planet);
        logger.info("Planet saved successfully from payload");
    }

    public Planet findPlanetById(String id) {
        logger.info("Finding planet with ID: {}", id);
        return planetDao.findById(id);
    }

    public List<Planet> findAll() {
        return planetDao.findAll();
    }

    public boolean updatePlanet(String id, Planet planet) {
        try {
            int rowsUpdated = planetDao.update(id, planet);
            return rowsUpdated > 0;
        } catch (Exception e) {
            logger.error("Failed to update planet with id: {}", id, e);
            return false;
        }
    }

    public boolean deletePlanet(String id) {
        try {
            logger.info("Deleting planet with id: {}", id);
            int rowsDeleted = planetDao.delete(id);
            if (rowsDeleted > 0) {
                logger.info("Planet deleted successfully");
                return true;
            } else {
                logger.warn("No planet found with id: {}", id);
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed to delete planet with id: {}", id, e);
            return false;
        }
    }
}