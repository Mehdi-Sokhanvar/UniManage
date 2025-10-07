//package org.unimanage.service.imple;
//
//import org.springframework.context.MessageSource;
//import org.springframework.stereotype.Service;
//import org.unimanage.domain.course.OfferedCourse;
//import org.unimanage.domain.course.Term;
//import org.unimanage.domain.user.Account;
//import org.unimanage.repository.AccountRepository;
//import org.unimanage.repository.OfferedCourseRepository;
//import org.unimanage.repository.PersonRepository;
//import org.unimanage.repository.TermRepository;
//import org.unimanage.service.CourseOfferingService;
//import org.unimanage.util.message.ErrorMessage;
//import org.unimanage.util.enumration.TermStatus;
//import org.unimanage.util.exception.AccessDeniedException;
//import org.unimanage.util.exception.EntityNotFoundException;
//
//import java.security.Principal;
//import java.util.List;
//
//@Service
//public class CourseOfferingServiceImpl extends BaseServiceImpl<OfferedCourse, Long> implements CourseOfferingService {
//
//    private final OfferedCourseRepository offeredCourseRepository;
//    private final TermRepository termRepository;
//    private final PersonRepository personRepository;
//    private final AccountRepository accountRepository;
//    private final MessageSource messageSource;
//
//    public CourseOfferingServiceImpl(OfferedCourseRepository courseOfferingService, TermRepository termRepository, PersonRepository personRepository, AccountRepository accountRepository, MessageSource messageSource) {
//        super(courseOfferingService,messageSource);
//        this.offeredCourseRepository = courseOfferingService;
//        this.termRepository = termRepository;
//        this.personRepository = personRepository;
//        this.accountRepository = accountRepository;
//        this.messageSource = messageSource;
//    }
//
//    @Override
//    protected void prePersist(OfferedCourse entity) {
//        validateTermAndCourse(entity);
//    }
//
//    @Override
//    protected void preUpdate(OfferedCourse entity) {
//        validateTermAndCourse(entity);
//    }
//
//    @Override
//    protected void preDelete(Long id) {
//        OfferedCourse offeredCourse = offeredCourseRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ENTITY_NOT_FOUND.format(id)));
//
//        validateTermStatus(offeredCourse.getTerm());
//    }
//
//    @Override
//    public boolean chooseCourseOffering(Principal principal, OfferedCourse course) {
//        String username = principal.getName();
//
//        OfferedCourse offeredCourse = offeredCourseRepository.findById(course.getId())
//                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ENTITY_NOT_FOUND.format(course.getId())));
//
//        validateTermStatus(offeredCourse.getTerm());
//
//        Account account = accountRepository.findByUsername(username)
//                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ENTITY_NOT_FOUND.format(username)));
//
//        if (!account.getPerson().getMajor().equals(offeredCourse.getTerm().getMajor())) {
//            throw new AccessDeniedException(ErrorMessage.YOU_CAN_CHOOSE_COURSE.format(
//                    account.getPerson().getMajor().getName()
//            ));
//        }
//
//        if (offeredCourse.getCapacity() <= 0) {
//            throw new AccessDeniedException(ErrorMessage.THE_CAPACITY_IS_FULL.format(
//                    offeredCourse.getCourse().getName()
//            ));
//        }
//
//        offeredCourse.setCapacity((byte) (offeredCourse.getCapacity() - 1));
//        offeredCourse.getStudentList().add(account.getPerson());
//
//        offeredCourseRepository.save(offeredCourse);
//        return true;
//    }
//
//    @Override
//    public List<OfferedCourse> getOfferedCourse(Principal principal) {
//        return List.of();
//    }
//
//    @Override
//    public List<OfferedCourse> getOfferedCourseByTermId(Long termId) {
//        return List.of();
//    }
//
//    private void validateTermAndCourse(OfferedCourse entity) {
//        Term term = termRepository.findById(entity.getTerm().getId())
//                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ENTITY_NOT_FOUND.format(entity.getTerm().getId())));
//
//        validateTermStatus(term);
//
//        if (!term.getMajor().getCourses().contains(entity.getCourse())) {
//            throw new IllegalStateException(ErrorMessage.COURSE_NOT_EXIST_IN_COURSE.format(entity.getCourse().getId()));
//        }
//    }
//
//    private void validateTermStatus(Term term) {
//        if (!(TermStatus.PLANNED.equals(term.getTermStatus()) ||
//                TermStatus.COURSE_OFFERING.equals(term.getTermStatus()))) {
//            throw new IllegalStateException(ErrorMessage.TERM_EXCEPTION.format(term.getTermStatus()));
//        }
//    }
//}
