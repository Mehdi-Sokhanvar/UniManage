package org.unimanage.util.dto.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.unimanage.domain.course.Major;
import org.unimanage.domain.user.Person;
import org.unimanage.domain.user.Role;
import org.unimanage.util.dto.PersonRegisterDto;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PersonMapper implements GenericMapper<Person, PersonRegisterDto> {


    public abstract Person toEntity(PersonRegisterDto dto);

    public abstract PersonRegisterDto toDTO(Person entity);

    @AfterMapping
    protected void afterToEntity(PersonRegisterDto dto, @MappingTarget Person person) {
        person.setMajor(Major.builder().name(dto.getMajorName()).build());
    }

    @AfterMapping
    protected void afterToDto(Person person, @MappingTarget PersonRegisterDto dto) {
        if (person.getMajor() != null) {
            dto.setMajorName(person.getMajor().getName());
        }
    }
}
