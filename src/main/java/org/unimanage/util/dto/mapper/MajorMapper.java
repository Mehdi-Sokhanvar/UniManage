package org.unimanage.util.dto.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.unimanage.domain.course.Major;
import org.unimanage.util.dto.MajorDto;

@Mapper(componentModel = "spring")
public abstract class MajorMapper implements GenericMapper<Major, MajorDto> {

    @Override
    public abstract MajorDto toDTO(Major entity);

    @Override
    public abstract Major toEntity(MajorDto majorDto);



}
