package org.unimanage.service.imple;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.unimanage.domain.course.Major;
import org.unimanage.domain.course.Term;
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.repository.AccountRepository;
import org.unimanage.repository.MajorRepository;
import org.unimanage.repository.PersonRepository;
import org.unimanage.repository.TermRepository;
import org.unimanage.service.TermService;
import org.unimanage.util.exception.AccessDeniedException;
import org.unimanage.util.exception.TermAlreadyExistsException;
import org.unimanage.util.enumration.TermStatus;
import org.unimanage.util.exception.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
public class TermServiceImpl extends BaseServiceImpl<Term, Long> implements TermService {

    private final TermRepository termRepository;
    private final MajorRepository majorRepository;
    private final MessageSource messageSource;
    private final PersonRepository personRepository;

    public TermServiceImpl(TermRepository termRepository, MajorRepository majorRepository, MessageSource messageSource, PersonRepository personRepository) {
        super(termRepository, messageSource);
        this.termRepository = termRepository;
        this.majorRepository = majorRepository;
        this.messageSource = messageSource;
        this.personRepository = personRepository;
    }


    @Override
    protected void prePersist(Term term) {
        Major major = majorRepository.findByName(term.getMajor().getName())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("entity.major.notfound", null, LocaleContextHolder.getLocale())));
        if (termRepository.existsBySemesterAndMajorIdAndYear(term.getSemester(), major.getId(), term.getAcademicCalendar().getCourseRegistrationStart().getYear())) {
            throw new TermAlreadyExistsException(messageSource.getMessage("term.exists", null, LocaleContextHolder.getLocale()));
        }
        term.setYear(term.getAcademicCalendar().getCourseRegistrationStart().getYear());
        term.setMajor(major);
    }


    @Override
    protected void preUpdate(Term term) {
        if (term.getAcademicCalendar().getCourseRegistrationStart().isAfter(LocalDate.now())) {
            throw new AccessDeniedException(messageSource.getMessage("access.denied.for.delete.term", null, LocaleContextHolder.getLocale()));
        }
        Major major = majorRepository.findByName(term.getMajor().getName())
                .orElseThrow(
                        () -> new EntityNotFoundException(messageSource.getMessage("entity.major.notfound", null, LocaleContextHolder.getLocale()))
                );
        term.setMajor(major);
    }


    @Override
    protected void preDelete(Long id) {
        Term term = termRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("entity.term.notfound", null, LocaleContextHolder.getLocale())));
        if (term.getAcademicCalendar().getCourseRegistrationStart().isAfter(LocalDate.now())) {
            throw new AccessDeniedException(messageSource.getMessage("access.denied.for.delete.term", null, LocaleContextHolder.getLocale()));
        }
    }


    @Override
    public List<Term> getAllTerms(String username) {
        Person personFounded = personRepository.findByNationalCode(username)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("person.not.founded", new Object[]{username}, LocaleContextHolder.getLocale())));
        return termRepository.findTermByMajor_Name(personFounded.getMajor().getName());
    }


}
