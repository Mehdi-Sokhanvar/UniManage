package org.unimanage.util.dto.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.web.bind.annotation.RequestMapping;
import org.unimanage.domain.course.Course;
import org.unimanage.domain.course.Major;
import org.unimanage.util.dto.CourseDto;


@Mapper(componentModel = "spring")
public abstract class CourseMapper implements GenericMapper<Course, CourseDto> {

    @Override
    public abstract CourseDto toDTO(Course entity);

    @Override
    public abstract Course toEntity(CourseDto courseRequest);


    @AfterMapping
    public void afterToEntity(CourseDto courseDto, @MappingTarget Course entity) {
        entity.setMajor(Major.builder().name(courseDto.getMajorName()).build());
    }

    @AfterMapping
    public void afterToDto(@MappingTarget CourseDto courseDto, Course entity) {
        courseDto.setMajorName(entity.getMajor().getName());
    }


}
