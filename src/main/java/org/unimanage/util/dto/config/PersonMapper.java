package org.unimanage.util.dto.config;

import org.mapstruct.Mapper;
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.util.dto.PersonRequestDto;


@Mapper(componentModel = "spring")
public interface PersonMapper extends GenericMapper<Person, PersonRequestDto> {
}
