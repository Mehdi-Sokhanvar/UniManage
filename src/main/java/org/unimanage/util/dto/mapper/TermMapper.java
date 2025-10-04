package org.unimanage.util.dto.mapper;

import org.aspectj.lang.annotation.After;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.unimanage.domain.course.Major;
import org.unimanage.domain.course.Term;
import org.unimanage.util.dto.TermDto;

import java.time.Instant;

@Mapper(componentModel = "spring")
public abstract class TermMapper implements GenericMapper<Term, TermDto> {

    @Override
    public abstract TermDto toDTO(Term entity);

    @Override
    public abstract Term toEntity(TermDto termDto);


    @AfterMapping
    public void AfterToEntity(TermDto termDto, @MappingTarget Term entity) {
        Major major = new Major();
        major.setId(termDto.getMajorId());
        entity.setStartTime(Instant.parse(termDto.getStartTime()));
        entity.setEndTime(Instant.parse(termDto.getEndTime()));
        entity.setMajor(major);
    }

    @AfterMapping
    public void AfterToDto(Term entity, @MappingTarget TermDto termDto) {
        termDto.setMajorId(entity.getMajor().getId());
        termDto.setMajorName(entity.getMajor().getName());
    }
}
