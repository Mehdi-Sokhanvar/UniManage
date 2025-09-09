package org.unimanage.util.dto.config;

import org.mapstruct.Mapper;
import org.unimanage.domain.user.Account;
import org.unimanage.util.dto.AccountDto;

@Mapper(componentModel = "spring")
public interface AccountMapper extends GenericMapper<Account, AccountDto> {
}
