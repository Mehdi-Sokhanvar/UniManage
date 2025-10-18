package org.unimanage.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.unimanage.domain.course.OfferedCourse;
import org.unimanage.domain.course.Term;
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.repository.AccountRepository;
import org.unimanage.repository.OfferedCourseRepository;
import org.unimanage.repository.PersonRepository;
import org.unimanage.repository.TermRepository;
import org.unimanage.util.dto.OfferedCourseTeacherDto;
import org.unimanage.util.exception.AccessDeniedException;
import org.unimanage.util.exception.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final TermRepository termRepository;
    private final AccountRepository accountRepository;
    private final OfferedCourseRepository offeredCourseRepository;
    private final PersonRepository personRepository;

    public boolean isManagerOfMajor(String majorName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person) authentication.getPrincipal();
        return person.getMajor().getName().equals(majorName);
    }

    public boolean isStudentOfMajor(Long termId) {

        Person person = personRepository.findByNationalCode(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        Term term = termRepository.findById(termId).orElseThrow(() -> new EntityNotFoundException("Term not found"));
        return term.getMajor().getName().equals(person.getMajor().getName());

    }

    public boolean isTeacherOfMajor(Long termId) {
        Person person = personRepository.findByNationalCode(SecurityContextHolder.getContext().getAuthentication().getName()).get();

        Term term = termRepository.findById(termId).orElseThrow(() -> new EntityNotFoundException("Term not found"));
        return term.getMajor().getName().equals(person.getMajor().getName());
    }

    public boolean isTeacherOfOfferedCourse(Long offeredCourseId) {
        Person person = personRepository.findByNationalCode(SecurityContextHolder.getContext().getAuthentication().getName()).get();

        OfferedCourse offeredCourse = offeredCourseRepository.findById(offeredCourseId).get();
        return offeredCourse.getTeacher().getId().equals(person.getId());
    }
}
