package org.unimanage.service.implementation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.domain.user.Role;
import org.unimanage.repository.AccountRepository;
import org.unimanage.repository.GenericRepository;
import org.unimanage.repository.PersonRepository;
import org.unimanage.repository.RoleRepository;
import org.unimanage.service.AuthService;
import org.unimanage.util.dto.AccountDto;
import org.unimanage.util.dto.AccountResponse;
import org.unimanage.util.dto.config.AccountMapper;
import org.unimanage.util.dto.config.AccountResponseMapper;
import org.unimanage.util.dto.config.GenericMapper;

import java.util.List;

@Service
public class AuthServiceImpl  implements AuthService {

    private final AccountRepository accountRepository;
    private final PersonRepository personRepository;
    private final AccountMapper accountMapper;
    private final RoleRepository roleRepository;
    private final AccountResponseMapper accountResponseMapper;


    public AuthServiceImpl(AccountRepository accountRepository,
                           PersonRepository personRepository, AccountMapper accountMapper,
                           RoleRepository roleRepository, AccountResponseMapper accountResponseMapper) {
        this.accountRepository = accountRepository;
        this.personRepository = personRepository;
        this.accountMapper = accountMapper;
        this.roleRepository = roleRepository;
        this.accountResponseMapper = accountResponseMapper;
    }

    @Override
    public AccountResponse registerStudent(AccountDto accountDto) {
        return register(accountDto, "STUDENT");
    }

    @Override
    public AccountResponse registerTeacher(AccountDto accountDto) {
        return register(accountDto, "Teacher");
    }

    private AccountResponse register(AccountDto accountDto, String roleName) {

        checkUserNameExists(accountDto.getUsername());
        Role role = checkRoleExists(roleName);
        Role role2 = checkRoleExists("USER");

        Account entity = accountMapper.toEntity(accountDto);
        entity.setPerson(Person.builder().roles(List.of(role, role2)).build());

        Account savedUser = accountRepository.save(entity);
        return accountResponseMapper.toDTO(savedUser);

    }

    private void checkUserNameExists(String username) {

        if (accountRepository.existsAccountByUsername(username)) {
            throw new RuntimeException("USERNAME ALREADY EXISTS");
        }

    }

    private Role checkRoleExists(String role) {
        return roleRepository.findByRoleName(role).orElseThrow(() -> new RuntimeException("ROLE NOT EXISTS"));
    }
}
