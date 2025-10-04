package org.unimanage.service.imple;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.unimanage.domain.BaseModel;
import org.unimanage.service.BaseService;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T extends BaseModel<ID>, ID extends Serializable>
        implements BaseService<T, ID> {

    private static final String ENTITY_NOT_FOUND = "Entity not found with ID: %s";

    private final JpaRepository<T, ID> repository;



    protected BaseServiceImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }


    @Override
    public T persist(T entity) {
        boolean isNew = entity.getId() == null;

        if (isNew) {
            prePersist(entity);
        } else {
            preUpdate(entity);
        }

        T saved = repository.save(entity);

        if (isNew) {
            postPersist(saved);
        } else {
            postUpdate(saved);
        }

        return saved;
    }

    @Override
    public T findById(ID id) {
        return repository.findById(id).orElseThrow(()-> new EntityNotFoundException(ENTITY_NOT_FOUND));
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(ID id) {
//   todo : توی برنامه ما وقتی داریم از جنریک برای ورود یه نوع مشخث میگیریم طوری بهتر این رو هندل کنیم
//        T entity = repository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        String.format(ENTITY_NOT_FOUND, id)
//                ));
        preDelete(id);
        repository.deleteById(id);
//        postDelete(entity);
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public Long count() {
        return repository.count();
    }

    protected void prePersist(T entity) {
    }

    protected void postPersist(T entity) {
    }

    protected void preUpdate(T entity) {
    }

    protected void postUpdate(T entity) {
    }

    protected void preDelete(ID id) {
    }

    protected void postDelete(T entity) {
    }

}