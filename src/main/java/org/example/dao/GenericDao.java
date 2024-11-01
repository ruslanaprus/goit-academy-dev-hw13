package org.example.dao;

import org.example.config.HibernateConfig;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

public class GenericDao<T, ID> {
    private static final Logger logger = LoggerFactory.getLogger(GenericDao.class);
    private final SessionFactory sessionFactory = HibernateConfig.getInstance().getSessionFactory();
    private final Class<T> entityClass;

    public GenericDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void save(T entity) {
        logger.debug("Opening session for saving entity: {}", entity);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            logger.debug("Entity saved successfully: {}", entity);
        }
    }

    public T findById(ID id) {
        logger.debug("Opening session for finding entity with ID: {}", id);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            var entity = session.get(entityClass, id);
            transaction.commit();
            return entity;
        }
    }

    public List<T> findAll() {
        logger.debug("Opening session for finding all entities of type {}", entityClass.getSimpleName());
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            var query = session.createQuery("from " + entityClass.getSimpleName(), entityClass);
            query.setFirstResult(0);
            query.setMaxResults(99);
            var list = query.getResultList();
            transaction.commit();
            return list;
        }
    }

    public int update(ID id, T updatedEntity) {
        logger.debug("Opening session for updating entity with ID: {}", id);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            T existingEntity = session.get(entityClass, id);
            if (existingEntity == null) {
                logger.warn("Entity with ID {} not found. Update aborted.", id);
                return 0;
            }

            for (Field field : entityClass.getDeclaredFields()) {
                field.setAccessible(true);

                try{
                    var updatedValue = field.get(updatedEntity);
                    if (updatedValue != null) {
                        field.set(existingEntity, updatedValue);
                    }
                } catch (IllegalAccessException e) {
                    logger.error("Failed to update field: {}, {}", field.getName(), e.getMessage());
                } finally {
                    field.setAccessible(false);
                }
            }
            transaction.commit();
            logger.debug("Entity with ID {} updated successfully", id);
            return 1;
        } catch (HibernateException e) {
            logger.error("Failed to update entity: {}, {}", id, e.getMessage());
            return 0;
        }
    }

    public int delete(ID id) {
        logger.debug("Opening session for deleting entity with ID: {}", id);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.remove(entity);
                transaction.commit();
                logger.debug("Entity with ID {} deleted successfully", id);
                return 1;
            } else {
                logger.warn("Entity with ID {} not found. Did not delete", id);
                return 0;
            }
        } catch (Exception e) {
            logger.error("Error while deleting entity with ID: {}, {}", id, e.getMessage());
            return 0;
        }
    }
}