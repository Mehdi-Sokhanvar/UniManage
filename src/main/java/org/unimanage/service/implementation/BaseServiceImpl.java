package org.unimanage.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.unimanage.repository.GenericRepository;
import org.unimanage.service.BaseService;
import org.unimanage.util.dto.config.GenericMapper;

import java.util.List;
import java.util.stream.Collectors;

//todo : is it true or false
public abstract class BaseServiceImpl<E, DTO, ID> implements BaseService<DTO, ID> {

    private final JpaRepository<E, ID> repository;
    private final GenericMapper<E, DTO> mapper;

    public BaseServiceImpl(JpaRepository<E, ID> repository, GenericMapper<E, DTO> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public DTO create(DTO dto) {
        E entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    public DTO getById(ID id) {
        return repository
                .findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Not found"));
    }

    @Override
    public DTO update(ID id, DTO dto) {
        E entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }

    @Override
    public List<DTO> getAll() {
        return repository
                .findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
