//package org.unimanage.service.imple;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.context.MessageSource;
//import org.springframework.stereotype.Service;
//import org.unimanage.domain.course.Major;
//import org.unimanage.domain.course.Term;
//import org.unimanage.repository.MajorRepository;
//import org.unimanage.repository.TermRepository;
//import org.unimanage.service.TermService;
//import org.unimanage.util.message.ErrorMessage;
//import org.unimanage.util.enumration.TermStatus;
//import org.unimanage.util.exception.EntityNotFoundException;
//import org.unimanage.util.exception.TimeProblemException;
//
//import java.time.Instant;
//
//@Service
//public class TermServiceImpl extends BaseServiceImpl<Term, Long> implements TermService {
//
//    private final TermRepository termRepository;
//    private final MajorRepository majorRepository;
//    private final MessageSource messageSource;
//
//
//    public TermServiceImpl(TermRepository termRepository, MajorRepository majorRepository, MessageSource messageSource) {
//        super(termRepository,messageSource);
//        this.termRepository = termRepository;
//        this.majorRepository = majorRepository;
//        this.messageSource = messageSource;
//    }
//
//
//    @Override
//    protected void prePersist(Term entity) {
//        validateTermTime(entity);
//        Major major = majorRepository.findById(entity.getMajor().getId())
//                .orElseThrow(() -> new EntityNotFoundException(
//                        ErrorMessage.ENTITY_NOT_FOUND.format(entity.getId())));
//        entity.setMajor(major);
//        entity.setTermStatus(TermStatus.PLANNED);
//    }
//
//    @Override
//    protected void preUpdate(Term entity) {
//        validateTermTime(entity);
//        Term persisted = termRepository.findById(entity.getId())
//                .orElseThrow(() -> new EntityNotFoundException(
//                        ErrorMessage.ENTITY_NOT_FOUND.format(entity.getId())
//                ));
//
//        Major major = majorRepository.findById(entity.getMajor().getId())
//                .orElseThrow(() -> new EntityNotFoundException(
//                        ErrorMessage.ENTITY_NOT_FOUND.format(entity.getId())
//                ));
////        entity.setVersion(persiste.getVersion());
//        entity.setMajor(major);
//
////        Major majorRef = entityManager.getReference(Major.class, entity.getMajor().getId());
////        entity.setMajor(majorRef);
//
//        validateTermStatus(persisted);
//
//
//    }
//
//
//    @Override
//    protected void preDelete(Long termId) {
//        Term term = termRepository.findById(termId)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        ErrorMessage.ENTITY_NOT_FOUND.format(termId)
//                ));
//        validateTermStatus(term);
//    }
//
//    // --- private helpers ---
//
//    private void validateTermStatus(Term term) {
//        if (!(TermStatus.COURSE_OFFERING.equals(term.getTermStatus())
//                || TermStatus.PLANNED.equals(term.getTermStatus()))) {
//            throw new TimeProblemException(ErrorMessage.TERM_EXCEPTION.format(term.getTermStatus()));
//        }
//    }
//
//    private void validateTermTime(Term entity) {
//
//
//        Instant now = Instant.now();
//        if (entity.getStartTime().isBefore(now)) {
//            throw new TimeProblemException(ErrorMessage.START_TIME_INVALID.format(entity.getStartTime()));
//        }
//        if (entity.getEndTime().isBefore(entity.getStartTime())) {
//            throw new TimeProblemException(ErrorMessage.END_TIME_INVALID.format(entity.getEndTime()));
//        }
//    }
//
//
//}
