package org.unimanage.service.imple;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.unimanage.domain.course.Course;
import org.unimanage.domain.course.OfferedCourse;
import org.unimanage.domain.course.Term;
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.domain.user.Role;
import org.unimanage.repository.*;
import org.unimanage.service.CourseOfferingService;
import org.unimanage.util.message.ErrorMessage;
import org.unimanage.util.enumration.TermStatus;
import org.unimanage.util.exception.AccessDeniedException;
import org.unimanage.util.exception.EntityNotFoundException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CourseOfferingServiceImpl extends BaseServiceImpl<OfferedCourse, Long> implements CourseOfferingService {

    private final OfferedCourseRepository offeredCourseRepository;
    private final TermRepository termRepository;
    private final CourseRepository courseRepository;
    private final PersonRepository personRepository;
    private final AccountRepository accountRepository;
    private final MessageSource messageSource;
    private final RoleRepository roleRepository;

    public CourseOfferingServiceImpl(OfferedCourseRepository courseOfferingService, TermRepository termRepository, CourseRepository courseRepository, PersonRepository personRepository, AccountRepository accountRepository, MessageSource messageSource, RoleRepository roleRepository) {
        super(courseOfferingService, messageSource);
        this.offeredCourseRepository = courseOfferingService;
        this.termRepository = termRepository;
        this.courseRepository = courseRepository;
        this.personRepository = personRepository;
        this.accountRepository = accountRepository;
        this.messageSource = messageSource;
        this.roleRepository = roleRepository;
    }

    @Override
    protected void prePersist(OfferedCourse entity) {
        canCourseAssignToTeacher(entity);
    }

    @Override
    protected void preUpdate(OfferedCourse entity) {
        canCourseAssignToTeacher(entity);
    }


    private void canCourseAssignToTeacher(OfferedCourse entity) {
        Term term = termRepository.findById(entity.getTerm().getId())
                .orElseThrow(() -> new EntityNotFoundException("Term not found"));
        Course course = courseRepository.findById(entity.getCourse().getId())
                .orElseThrow(() -> new EntityNotFoundException("Term not found"));
        Person teacher = personRepository.findById(entity.getTeacher().getId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
        if (!teacher.getRoles().contains(roleRepository.findByName("TEACHER").get())) {
            throw new AccessDeniedException("Access denied");
        }
        List<OfferedCourse> offeredCourses =
                offeredCourseRepository.findByTeacherIdAndDayOfWeek(teacher.getId(), entity.getDayOfWeek());
        for (OfferedCourse existing : offeredCourses) {
            boolean overlap = entity.getStartTime().isBefore(existing.getEndTime()) &&
                    entity.getEndTime().isAfter(existing.getStartTime());
            if (overlap) {
                throw new AccessDeniedException("Access denied");
            }
        }
        entity.setTerm(term);
        entity.setCourse(course);
        entity.setTeacher(teacher);
    }

    @Override
    protected void preDelete(Long offeredCourseId) {
        OfferedCourse offeredCourse = offeredCourseRepository.findById(offeredCourseId)
                .orElseThrow(() -> new EntityNotFoundException("Offered course not found"));
        if (offeredCourse.getTerm().getAcademicCalendar().getCourseRegistrationStart()
                .isAfter(LocalDate.now())) {
            throw new AccessDeniedException("Access denied");
        }
    }


}
