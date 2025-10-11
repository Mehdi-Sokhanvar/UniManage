package org.unimanage.service.imple;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.unimanage.domain.course.Major;
import org.unimanage.domain.course.Term;
import org.unimanage.domain.user.Account;
import org.unimanage.repository.AccountRepository;
import org.unimanage.repository.MajorRepository;
import org.unimanage.repository.TermRepository;
import org.unimanage.service.TermService;
import org.unimanage.util.exception.AccessDeniedException;
import org.unimanage.util.exception.TermAlreadyExistsException;
import org.unimanage.util.message.ErrorMessage;
import org.unimanage.util.enumration.TermStatus;
import org.unimanage.util.exception.EntityNotFoundException;
import org.unimanage.util.exception.TimeProblemException;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

@Service
public class TermServiceImpl extends BaseServiceImpl<Term, Long> implements TermService {

    private final TermRepository termRepository;
    private final MajorRepository majorRepository;
    private final MessageSource messageSource;
    private final AccountRepository accountRepository;

    public TermServiceImpl(TermRepository termRepository, MajorRepository majorRepository, MessageSource messageSource, AccountRepository accountRepository) {
        super(termRepository, messageSource);
        this.termRepository = termRepository;
        this.majorRepository = majorRepository;
        this.messageSource = messageSource;
        this.accountRepository = accountRepository;
    }


    @Override
    protected void prePersist(Term entity) {
        Major major = majorRepository.findByName(entity.getMajor().getName())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("entity.major.notfound", null, LocaleContextHolder.getLocale())));
        if (termRepository.existsByTermTypeAndMajorIdAndYear(entity.getTermType(),major.getId(),entity.getYear())) {
            throw new TermAlreadyExistsException(messageSource.getMessage("term.exists", null, LocaleContextHolder.getLocale()));
        }
        entity.setTermStatus(TermStatus.INACTIVE);
        entity.setMajor(major);
    }


    @Override
    protected void preUpdate(Term entity) {
        Major major = majorRepository.findByName(entity.getMajor().getName())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("entity.major.notfound", null, LocaleContextHolder.getLocale())));
        entity.setMajor(major);
    }


    @Override
    protected void preDelete(Long id) {
        Term term = termRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("entity.term.notfound", null, LocaleContextHolder.getLocale())));
        if (term.getTermStatus().equals(TermStatus.ACTIVE)) {
            throw new AccessDeniedException(messageSource.getMessage("access.denied.for.delete.term", null, LocaleContextHolder.getLocale()));
        }
    }


    @Override
    public List<Term> getAllTerms(Principal principal) {
        String userName = principal.getName();
        Account account = accountRepository.findByUsername(userName).get();
//        return termRepository.findTermByMajor_Name()
//                .orElseThrow(()-> new EntityNotFoundException(messageSource.getMessage("",null,null)));

        return null;
    }
}
