package org.unimanage.util.dto.mapper;


public interface GenericMapper <E,DTO>{
    DTO toDTO(E entity);
    E toEntity(DTO dto);
}
