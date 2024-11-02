package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.config.HibernateConfig;
import org.example.dao.GenericDao;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GenericService<T, ID> implements IGenericService<T, ID> {
    private static final Logger logger = LoggerFactory.getLogger(GenericService.class);
    private final GenericDao<T, ID> dao;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Class<T> entityClass;

    public GenericService(Class<T> entityClass) {
        this.entityClass = entityClass;
        SessionFactory sessionFactory = HibernateConfig.getInstance().getSessionFactory();
        this.dao = new GenericDao<>(entityClass, sessionFactory);
    }

    @Override
    public void save(T entity) {
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

    public <R> List<R> findWithQuery(String hql, Class<R> resultClass, Map<String, Object> params){
        return dao.findWithQuery(hql, resultClass, params);
    }
}
