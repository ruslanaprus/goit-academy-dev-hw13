package org.example.service;

import java.util.List;

public interface IGenericService<T, ID> {
    void save(T entity);
    T findById(ID id);
    List<T> findAll();
    boolean update(ID id, T entity);
    boolean delete(ID id);
}
