package org.unimanage.service;

import org.unimanage.domain.user.Account;
import org.unimanage.util.dto.AccountDto;
import org.unimanage.util.dto.AccountResponse;

public interface AuthService {

    AccountResponse registerStudent(AccountDto accountDto);
    AccountResponse registerTeacher(AccountDto accountDto);
}
