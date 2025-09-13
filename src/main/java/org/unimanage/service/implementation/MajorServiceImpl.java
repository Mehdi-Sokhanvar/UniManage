package org.unimanage.service.implementation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unimanage.domain.course.Major;
import org.unimanage.service.BaseService;

public class MajorServiceImpl extends BaseServiceImpl<Major,Long> implements BaseService<Major,Long> {


    public MajorServiceImpl(JpaRepository<Major, Long> repository) {
        super(repository);
    }

    @Override
    protected void prePersist(Major entity) {

    }

    @Override
    protected void preUpdate(Major entity) {

    }

    @Override
    protected void preDelete(Long aLong) {

    }

    @Override
    protected void postUpdate(Major entity) {

    }

    @Override
    protected void postPersist(Major entity) {

    }

    @Override
    protected void postDelete(Major entity) {

    }
}
