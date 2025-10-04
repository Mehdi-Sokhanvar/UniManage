package org.unimanage.util.dto.mapper;

import org.mapstruct.Mapper;
import org.unimanage.domain.user.Account;
import org.unimanage.util.dto.AccountResponse;

@Mapper(componentModel = "spring")
public interface AccountResponseMapper extends GenericMapper<Account, AccountResponse>{
}
