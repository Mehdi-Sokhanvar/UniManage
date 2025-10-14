package org.unimanage.util.dto.mapper;


import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import org.unimanage.domain.course.Course;
import org.unimanage.domain.course.OfferedCourse;
import org.unimanage.domain.course.Term;
import org.unimanage.domain.user.Person;
import org.unimanage.util.dto.OfferedCourseDTO;

@Mapper(componentModel = "spring")
public abstract class OfferedCourseMapper implements GenericMapper<OfferedCourse, OfferedCourseDTO> {

    public abstract OfferedCourseDTO toDTO(OfferedCourse entity);

    public abstract OfferedCourse toEntity(OfferedCourseDTO offeredCourseDTO);

    @AfterMapping
    public void toEntity(@MappingTarget OfferedCourse offeredCourse, OfferedCourseDTO dto) {
        offeredCourse.setTeacher(
                Person.builder().id(dto.getTeacherId()).build()
        );

        offeredCourse.setCourse(
                Course.builder().id(dto.getCourseId()).build()
        );

        offeredCourse.setTerm(
                Term.builder().id(dto.getTermId()).build()
        );
    }
}
