package org.unimanage.util.dto.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.unimanage.domain.course.OfferedCourse;
import org.unimanage.util.dto.StudentScheduleDto;

@Mapper(componentModel = "spring")
public abstract class StudentScheduleMapper implements GenericMapper<OfferedCourse, StudentScheduleDto> {


    @Override
    public abstract OfferedCourse toEntity(StudentScheduleDto studentScheduleDto);

    @Override
    public abstract StudentScheduleDto toDTO(OfferedCourse entity);

    @AfterMapping
    public void toDto(OfferedCourse entity, @MappingTarget StudentScheduleDto dto) {
        dto.setCourseId(entity.getId());
        dto.setCapacity(entity.getCapacity());
        dto.setEndTime(entity.getEndTime());
        dto.setStartTime(entity.getStartTime());
        dto.setCourseName(entity.getCourse().getName());
        dto.setTeacherName(entity.getTeacher().getFirstName().concat(" ").concat(entity.getTeacher().getLastName()));
        dto.setDayOfWeek(entity.getDayOfWeek());
    }
}
