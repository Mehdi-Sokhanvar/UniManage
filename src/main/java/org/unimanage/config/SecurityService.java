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

    public boolean isManagerOfMajor(String majorName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person) authentication.getPrincipal();
        return person.getMajor().getName().equals(majorName);
    }

    public boolean isStudentOfMajor(Long termId) {
        Account account = accountRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()).get();
        Term term = termRepository.findById(termId).orElseThrow(() -> new EntityNotFoundException("Term not found"));
        return term.getMajor().getName().equals(account.getPerson().getMajor().getName());

    }

    public boolean isTeacherOfMajor(Long termId) {
        Account account = accountRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()).get();
        Term term = termRepository.findById(termId).orElseThrow(() -> new EntityNotFoundException("Term not found"));
        return term.getMajor().getName().equals(account.getPerson().getMajor().getName());
    }

    public boolean isTeacherOfOfferedCourse(Long offeredCourseId) {
        Account account = accountRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()).get();
        OfferedCourse offeredCourse = offeredCourseRepository.findById(offeredCourseId).get();
        return offeredCourse.getTeacher().getId().equals(account.getPerson().getId());
    }
}
