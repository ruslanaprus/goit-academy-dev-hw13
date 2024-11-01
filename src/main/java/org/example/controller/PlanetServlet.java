package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Planet;
import org.example.service.PlanetService;
import org.example.config.TemplateConfig;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {
        "/planetManager",
        "/createPlanetForm",
        "/findPlanetByIdForm",
        "/updatePlanetForm",
        "/deletePlanetByIdForm",
        "/findPlanetById",
        "/findAllPlanets",
        "/createPlanet",
        "/updatePlanet",
        "/deletePlanetById"})
public class PlanetServlet extends HttpServlet {
    private final TemplateConfig templateConfig = new TemplateConfig();
    private final PlanetService planetService = new PlanetService();

    public PlanetServlet() throws ServletException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        Context context = new Context();

        switch (servletPath) {
            case "/planetManager" -> templateConfig.process("planet", context, res);
            case "/createPlanetForm" -> {
                context.setVariable("action", "createPlanetForm");
                templateConfig.process("planet", context, res);
            }
            case "/findPlanetByIdForm" -> {
                context.setVariable("action", "findPlanetByIdForm");
                templateConfig.process("planet", context, res);
            }
            case "/findPlanetById" -> {
                String planetId = req.getParameter("planetId");
                Planet planetById = planetService.findPlanetById(planetId);
                if (planetById != null) {
                    context.setVariable("planet", planetById);
                    context.setVariable("message", "Planet found successfully.");
                } else {
                    context.setVariable("message", "Planet not found.");
                }
                context.setVariable("action", "planetDetails");
                templateConfig.process("planet", context, res);
            }
            case "/findAllPlanets" -> {
                List<Planet> planets = planetService.findAll();
                context.setVariable("planets", planets);
                context.setVariable("action", "allPlanetDetails");
                templateConfig.process("planet", context, res);
            }
            case "/updatePlanetForm" -> {
                context.setVariable("action", "updatePlanetForm");
                templateConfig.process("planet", context, res);
            }
            case "/updatePlanet" -> {
                String planetId = req.getParameter("planetId");
                String name = req.getParameter("name");
                Planet updatedPlanet = new Planet();
                boolean isUpdated = false;

                if (name != null && !name.isEmpty()) {
                    updatedPlanet.setName(name);
                }
                if (planetId != null && !planetId.isEmpty()) {
                    updatedPlanet.setId(planetId);
                }

                try {
                    isUpdated = planetService.updatePlanet(planetId, updatedPlanet);
                    context.setVariable("message", isUpdated ? "Planet updated successfully." : "Failed to update planet.");
                } catch (Exception e) {
                    context.setVariable("message", "Error updating planet: " + e.getMessage());
                }

                context.setVariable("action", "updatePlanet");
                templateConfig.process("planet", context, res);
            }
            case "/deletePlanetByIdForm" -> {
                context.setVariable("action", "deletePlanetByIdForm");
                templateConfig.process("planet", context, res);
            }
            case "/deletePlanetById" -> {
                String planetId = req.getParameter("planetId");
                boolean isDeleted = planetService.deletePlanet(planetId);
                context.setVariable("message", isDeleted ? "Planet deleted successfully." : "Failed to delete planet.");
                context.setVariable("action", "deletePlanetById");
                templateConfig.process("planet", context, res);
            }
            default -> templateConfig.process("planet", context, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Context context = new Context();
        try {
            planetService.savePlanet(req.getReader());
            context.setVariable("message", "Planet created successfully.");
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            context.setVariable("message", "Error creating planet: " + e.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        context.setVariable("action", "createPlanet");
        templateConfig.process("planet", context, res);
    }
}