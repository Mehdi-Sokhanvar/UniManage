package org.unimanage.util.dto.config;


import org.mapstruct.Mapper;

public interface GenericMapper <E,DTO>{
    DTO toDTO(E entity);
    E toEntity(DTO dto);
}
