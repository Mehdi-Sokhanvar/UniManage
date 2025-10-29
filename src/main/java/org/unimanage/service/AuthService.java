package org.unimanage.service;

import org.unimanage.domain.user.Person;
import org.unimanage.domain.user.Role;
import org.unimanage.util.dto.AccountRequestDto;
import org.unimanage.util.dto.AuthResponseDto;

import java.security.Principal;
import java.util.Set;

public interface AuthService  extends BaseService<Person,Long>{
    AuthResponseDto login(AccountRequestDto request);
    void addRoleToPerson(String role , Long personId);
    Set<Role> getPersonRoles(Principal principal);
    void rePassword(String oldPassword, String newPassword, Principal principal);
    void forgotPassword(String emailOrPhone);
    AuthResponseDto refreshToken(String refreshToken);
    void activeAccount(Long accountId);
    void logOut(String token);
}
