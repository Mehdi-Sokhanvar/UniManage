package org.unimanage.service.implementation;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.unimanage.domain.course.Major;
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.domain.user.Role;
import org.unimanage.repository.AccountRepository;
import org.unimanage.repository.MajorRepository;
import org.unimanage.repository.PersonRepository;
import org.unimanage.repository.RoleRepository;
import org.unimanage.service.AuthService;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl extends BaseServiceImpl<Person, Long> implements AuthService {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final MajorRepository majorRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(PersonRepository personRepository, RoleRepository roleRepository, MajorRepository majorRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        super(personRepository);
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.majorRepository = majorRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    protected void prePersist(Person entity) {
        Optional<Person> byNationalCode = personRepository
                .findByNationalCode(entity.getNationalCode());
        if (byNationalCode.isPresent()) {
            throw new EntityExistsException("Person with NationalCode " + entity.getNationalCode() + " already exists");
        }
        List<Role> persistedRoles = entity.getRoles().stream()
                .map(role -> roleRepository.findByRoleName(role.getRoleName())
                        .orElseThrow(() -> new EntityNotFoundException("Role " + role.getRoleName() + " not found")))
                .toList();
        entity.setRoles(persistedRoles);
    }

    public Person registerStudent(Person student) {
        student.setRoles(List.of(roleRepository.findByRoleName("STUDENT")
                .orElseThrow(() -> new EntityNotFoundException("Role STUDENT not found"))));
        return this.persist(student);
    }


    public Person addTeacher(Person teacher) {
        teacher.setRoles(List.of(roleRepository.findByRoleName("TEACHER")
                .orElseThrow(() -> new EntityNotFoundException("Role TEACHER not found"))));
        return this.persist(teacher);
    }

    @Override
    protected void preUpdate(Person entity) {
    }

    @Override
    protected void preDelete(Long aLong) {

    }

    @Override
    protected void postUpdate(Person entity) {
    }

    @Override
    protected void postPersist(Person entity) {
        accountRepository.save(Account.builder()
                .person(entity)
                .username(entity.getNationalCode())
                .password(passwordEncoder.encode(entity.getPhoneNumber()))
                .build());
    }

    @Override
    protected void postDelete(Person entity) {

    }
}
