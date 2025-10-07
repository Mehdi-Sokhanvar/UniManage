package org.unimanage.util.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.unimanage.domain.course.Major;
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.domain.user.Role;
import org.unimanage.repository.AccountRepository;
import org.unimanage.repository.MajorRepository;
import org.unimanage.repository.RoleRepository;
import org.unimanage.util.enumration.AccountStatus;


import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final MajorRepository majorRepository;

    public DataInitializer(RoleRepository roleRepository, PasswordEncoder passwordEncoder, AccountRepository accountRepository, MajorRepository majorRepository) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.majorRepository = majorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
        Role studentRole = roleRepository.save(Role.builder().name("STUDENT").build());
        Role instructorRole = roleRepository.save(Role.builder().name("TEACHER").build());
        Role managerRole = roleRepository.save(Role.builder().name("MANAGER").build());
        Role userRole = roleRepository.save(Role.builder().name("USER").build());
        accountRepository.save(
                Account.builder()
                        .person(Person.builder()
                                .nationalCode("123456789")
                                .email("admin@gmail.com")
                                .build())
                        .username("admin")
                        .password(passwordEncoder.encode("1234567890"))

                        .authId(UUID.fromString("a2fa45dd-8ffb-4e20-8b32-313a5bb046bc"))
                        .person(Person.builder().roles(Set.of(adminRole, userRole)).build())
                        .status(AccountStatus.ACTIVE)

                        .build()
        );
        accountRepository.save(
                Account.builder()
                        .person(Person.builder()
                                .nationalCode("123456789")
                                .email("admin@gmail.com")
                                .build())
                        .username("manager")
                        .password(passwordEncoder.encode("1234567890"))
                        .person(Person.builder().roles(Set.of(managerRole, userRole)).build())

                        .status(AccountStatus.ACTIVE)
                        .build()
        );

        majorRepository.save(Major.builder()
                .name("علوم کامپیوتر")
                .numberOfUnits(70)
                .build());
        majorRepository.save(Major.builder()
                .name("الکترونیک ")
                .numberOfUnits( 70)
                .build());
        majorRepository.save(Major.builder()
                .name("برق ")
                .numberOfUnits(70)
                .build());


    }
}
