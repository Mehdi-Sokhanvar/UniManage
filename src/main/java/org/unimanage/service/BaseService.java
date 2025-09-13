package org.unimanage.service;

import org.unimanage.domain.BaseModel;

import java.util.List;
import java.util.Optional;

public interface BaseService<T extends BaseModel, ID> {

    T persist(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    void deleteById(ID id);

    boolean existsById(ID id);

    Long count();

}
