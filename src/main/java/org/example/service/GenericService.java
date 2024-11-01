package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.GenericDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class GenericService<T, ID> implements IGenericService<T, ID> {
    private static final Logger logger = LoggerFactory.getLogger(GenericService.class);
    private final GenericDao<T, ID> dao;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Class<T> entityClass;

    public GenericService(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.dao = new GenericDao<>(entityClass);
    }

    @Override
    public void save(T entity) {
        logger.info("Saving entity: {}", entity);
        dao.save(entity);
    }

    @Override
    public void save(BufferedReader payload) {
        T entity = null;
        try {
            entity = objectMapper.readValue(payload, entityClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("Saving entity from payload: {}", entity);
        dao.save(entity);
        logger.info("Entity saved successfully from payload");
    }

    @Override
    public T findById(ID id) {
        logger.info("Finding entity by id: {}", id);
        return dao.findById(id);
    }

    @Override
    public List<T> findAll() {
        logger.info("Finding all entities");
        return dao.findAll();
    }

    @Override
    public boolean update(ID id, T entity) {
        try{
            int rowsUpdated = dao.update(id, entity);
            return rowsUpdated > 0;
        } catch (Exception e) {
            logger.error("Failed to update entity with id: {}, {}", id, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(ID id) {
        try {
            int rowsDeleted = dao.delete(id);
            return rowsDeleted > 0;
        } catch (Exception e) {
            logger.error("Failed to delete entity with id: {}, {}", id, e.getMessage());
            return false;
        }
    }
}
