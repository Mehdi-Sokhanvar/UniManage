package org.unimanage.service.imple;

import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unimanage.domain.course.*;
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.repository.*;
import org.unimanage.service.CourseOfferingService;
import org.unimanage.util.dto.CourseRegistrationDTO;
import org.unimanage.util.dto.StudentCourse;
import org.unimanage.util.exception.AccessDeniedException;
import org.unimanage.util.exception.EntityNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CourseOfferingServiceImpl extends BaseServiceImpl<OfferedCourse, Long> implements CourseOfferingService {

    private final OfferedCourseRepository offeredCourseRepository;
    private final TermRepository termRepository;
    private final CourseRepository courseRepository;
    private final PersonRepository personRepository;
    private final AccountRepository accountRepository;
    private final MessageSource messageSource;
    private final RoleRepository roleRepository;
    private final StudentRegistrationRepository studentRegistrationRepository;

    public CourseOfferingServiceImpl(OfferedCourseRepository courseOfferingService, TermRepository termRepository, CourseRepository courseRepository, PersonRepository personRepository, AccountRepository accountRepository, MessageSource messageSource, RoleRepository roleRepository, StudentRegistrationRepository studentRegistrationRepository) {
        super(courseOfferingService, messageSource);
        this.offeredCourseRepository = courseOfferingService;
        this.termRepository = termRepository;
        this.courseRepository = courseRepository;
        this.personRepository = personRepository;
        this.accountRepository = accountRepository;
        this.messageSource = messageSource;
        this.roleRepository = roleRepository;
        this.studentRegistrationRepository = studentRegistrationRepository;
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
//        if (!teacher.getRoles().contains(roleRepository.findByName("TEACHER").get())) {
//            throw new AccessDeniedException("Access denied");
//        }
//        List<OfferedCourse> offeredCourses =
//                offeredCourseRepository.findByTeacherIdAndDayOfWeek(teacher.getId(), entity.getDayOfWeek());
//        for (OfferedCourse existing : offeredCourses) {
//            boolean overlap = entity.getStartTime().isBefore(existing.getEndTime()) &&
//                    entity.getEndTime().isAfter(existing.getStartTime());
//            if (overlap) {
//                throw new AccessDeniedException("Access denied");
//            }   // this code handle teacher schedule in application layer but i handled in database layer
//        }
        if (offeredCourseRepository.isTeacherOverlappingSchedule(teacher.getId(), term.getId(), entity.getDayOfWeek(), entity.getStartTime(), entity.getEndTime())) {
            throw new AccessDeniedException("Access denied");
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


    @Override
    public List<OfferedCourse> findOfferedCoursesByTermId(Long termId) {
        return offeredCourseRepository.findByTermId(termId);
    }

    @Transactional
    @Override
    public void getStudentCourse(Long courseId) {
        OfferedCourse offeredCourse = offeredCourseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        if (offeredCourse.getTerm().getAcademicCalendar().getCourseRegistrationStart().isBefore(LocalDate.now())
                || offeredCourse.getTerm().getAcademicCalendar().getCourseRegistrationEnd().isAfter(LocalDate.now())) {
            throw new AccessDeniedException("Access denied s");
        }
        if (offeredCourse.getTerm().getAcademicCalendar().getAddDropStart().isBefore(LocalDate.now())
                || offeredCourse.getTerm().getAcademicCalendar().getAddDropEnd().isAfter(LocalDate.now())) {
            throw new AccessDeniedException("Access denied e");
        }
        Account account = accountRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();

//        if (studentRegistrationRepository.existsByStudentIdAndCourseIdAndCourseStatusIn
//                (account.getPerson().getId(), courseId, List.of(CourseStatus.PASSED, CourseStatus.IN_PROGRESS))) {
//            throw new AccessDeniedException("Access denied c");
//        }
        offeredCourse.setCapacity(offeredCourse.getCapacity() - 1);
        studentRegistrationRepository.save(
                StudentCourseRegistration.builder()
                        .course(offeredCourse)
//                        .student(account.getPerson())
                        .registration(LocalDateTime.now())
                        .courseStatus(CourseStatus.IN_PROGRESS)
                        .grade(0.0)
                        .build()
        );
        offeredCourseRepository.save(offeredCourse);
    }

    @Transactional
    @Override
    public void deleteStudentCourse(Long courseId) {
//        OfferedCourse offeredCourse = offeredCourseRepository.findById(courseId)
//                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
//        if (offeredCourse.getTerm().getAcademicCalendar().getCourseRegistrationStart().isBefore(LocalDate.now())
//                || offeredCourse.getTerm().getAcademicCalendar().getCourseRegistrationEnd().isAfter(LocalDate.now())) {
//            throw new AccessDeniedException("Access denied s");
//        }
//        if (offeredCourse.getTerm().getAcademicCalendar().getAddDropStart().isBefore(LocalDate.now())
//                || offeredCourse.getTerm().getAcademicCalendar().getAddDropEnd().isAfter(LocalDate.now())) {
//            throw new AccessDeniedException("Access denied e");
//        }
//        Account account = accountRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
//
//        offeredCourse.setCapacity(offeredCourse.getCapacity() + 1);
//
//        offeredCourse.getStudentCourseRegistrations().remove(account.getPerson());
//        offeredCourseRepository.deleteById();
    }

    @Override
    public List<CourseRegistrationDTO> findAllCourseByTermId(Long termId) {
//        Account account = accountRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
//        List<CourseRegistrationDTO> courseRegistrationDTO = new ArrayList<>();
//        for (StudentCourseRegistration studentCourseRegistration : studentRegistrationRepository.findByCourse_Term_Id_AndStudent_Id(account.getPerson().getId(), termId)) {
//
//            courseRegistrationDTO.add(CourseRegistrationDTO.builder()
//                    .courseName(studentCourseRegistration.getCourse().getCourse().getName())
//                    .teacherName(studentCourseRegistration.getCourse().getTeacher().getFirstName().concat(" ")
//                            .concat(studentCourseRegistration.getCourse().getTeacher().getLastName()))
//                    .dayOfWeek(studentCourseRegistration.getCourse().getDayOfWeek())
//                    .startTime(studentCourseRegistration.getCourse().getStartTime())
//                    .endTime(studentCourseRegistration.getCourse().getEndTime())
//                    .score(studentCourseRegistration.getGrade())
//                    .courseStatus(studentCourseRegistration.getCourseStatus())
//                    .build());
//
//        }
//
//        return courseRegistrationDTO;  i think this code is not good because use a lot of getter (N+1 Query Problem)

        Account account = accountRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
//        return studentRegistrationRepository.findAllStudentCourse(account.getPerson().getId(), termId);
        return null;
    }

    @Override
    public List<OfferedCourse> getTeacherScheduleTime(Long termId) {
//        Account account = accountRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
//        return offeredCourseRepository.findAllByTeacher_IdAndTerm_Id(account.getPerson().getId(), termId);
        return null;
    }

    @Override
    public List<StudentCourse> getAllStudentsByOfferedCourseId(Long offeredCourseId) {
        return offeredCourseRepository.findById(offeredCourseId).get().getStudentCourseRegistrations().stream()
                .map(student -> StudentCourse.builder()
                        .firstName(student.getStudent().getFirstName().concat(" ").concat(student.getStudent().getLastName()))
                        .lastName(student.getStudent().getLastName())
                        .nationalCode(student.getStudent().getNationalCode())
                        .build())
                .toList();
    }




}



