package org.unimanage.util.dto.mapper;

import org.aspectj.lang.annotation.After;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.unimanage.domain.course.AcademicCalendar;
import org.unimanage.domain.course.Major;
import org.unimanage.domain.course.Term;
import org.unimanage.util.dto.TermDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public abstract class TermMapper implements GenericMapper<Term, TermDto> {

    @Override
    public abstract TermDto toDTO(Term entity);

    @Override
    public abstract Term toEntity(TermDto termDto);


    @AfterMapping
    public void AfterToEntity(TermDto termDto, @MappingTarget Term entity) {
        entity.setMajor(Major.builder()
                .name(termDto.getMajorName())
                .build());
        entity.setAcademicCalendar(AcademicCalendar.builder()
                .classesEnd(termDto.getClassesEnd())
                .classesStart(termDto.getClassesStart())
                .courseRegistrationStart(termDto.getCourseRegistrationStart())
                .courseRegistrationEnd(termDto.getCourseRegistrationEnd())
                .addDropEnd(termDto.getAddDropEnd())
                .addDropStart(termDto.getAddDropStart())
                .build());
    }

    @AfterMapping
    public void AfterToDto(Term entity, @MappingTarget TermDto termDto) {
        termDto.setMajorName(entity.getMajor().getName());

        termDto.setCourseRegistrationStart(entity.getAcademicCalendar().getCourseRegistrationStart());
        termDto.setCourseRegistrationEnd(entity.getAcademicCalendar().getCourseRegistrationEnd());
        termDto.setClassesStart(entity.getAcademicCalendar().getClassesStart());
        termDto.setClassesEnd(entity.getAcademicCalendar().getClassesEnd());
        termDto.setAddDropEnd(entity.getAcademicCalendar().getAddDropEnd());
        termDto.setAddDropStart(entity.getAcademicCalendar().getAddDropStart());


    }
}
