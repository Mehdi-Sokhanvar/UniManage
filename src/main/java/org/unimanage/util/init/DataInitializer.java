package org.unimanage.util.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.domain.user.Role;
import org.unimanage.repository.AccountRepository;
import org.unimanage.repository.RoleRepository;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public DataInitializer(RoleRepository roleRepository, PasswordEncoder passwordEncoder, AccountRepository accountRepository) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = roleRepository.save(Role.builder().roleName("ADMIN").build());
        Role studentRole = roleRepository.save(Role.builder().roleName("STUDENT").build());
        Role instructorRole = roleRepository.save(Role.builder().roleName("TEACHER").build());
        Role userRole = roleRepository.save(Role.builder().roleName("USER").build());
        accountRepository.save(
                Account.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("1234567890"))
                        .email("admin@gmail.com")
                        .person(Person.builder().roles(List.of(adminRole,userRole)).build())
                        .build()
        );
    }
}
