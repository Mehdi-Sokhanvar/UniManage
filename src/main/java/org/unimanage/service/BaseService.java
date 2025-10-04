package org.unimanage.service;


import org.unimanage.domain.BaseModel;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseService<T extends BaseModel<ID>, ID extends Serializable> {

    T persist(T entity);

    T findById(ID id);

    List<T> findAll();

    void deleteById(ID id);

    boolean existsById(ID id);

    Long count();

}
