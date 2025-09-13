package org.unimanage.service.implementation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unimanage.domain.BaseModel;
import org.unimanage.service.BaseService;

import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T extends BaseModel, ID> implements BaseService<T, ID> {

    private final JpaRepository<T, ID> repository;

    public BaseServiceImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }


    @Override
    public T persist(T entity) {

        prePersist(entity);
        T save = repository.save(entity);

        postPersist(save);
        return save;
    }

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(ID id) {
        preDelete(id);
        repository.findById(id).ifPresent(repository::delete);
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public Long count() {
        return repository.count();
    }

    protected abstract void prePersist(T entity);

    protected abstract void preUpdate(T entity);

    protected abstract void preDelete(ID id);

    protected abstract void postUpdate(T entity);
    protected abstract void postPersist(T entity);

    protected abstract void postDelete(T entity);

}
