package org.unimanage.util.dto.mapper;


import org.mapstruct.Mapper;
import org.unimanage.domain.user.Account;
import org.unimanage.util.dto.AccountRequestDto;

@Mapper(componentModel = "spring")
public interface AccountRequestMapper extends GenericMapper<Account, AccountRequestDto> {
}
