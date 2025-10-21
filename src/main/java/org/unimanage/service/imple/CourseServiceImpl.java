package org.unimanage.service.imple;


import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.unimanage.domain.course.Course;
import org.unimanage.domain.course.Major;
import org.unimanage.domain.user.Person;
import org.unimanage.repository.CourseRepository;
import org.unimanage.repository.MajorRepository;
import org.unimanage.repository.PersonRepository;
import org.unimanage.service.CourseService;
import org.unimanage.util.exception.EntityNotFoundException;
import org.unimanage.util.message.ErrorMessage;
import org.unimanage.util.exception.AccessDeniedException;
import org.unimanage.util.exception.DuplicateEntityException;

import java.util.List;

@Service
public class CourseServiceImpl extends BaseServiceImpl<Course, Long> implements CourseService {


    private final CourseRepository courseRepository;
    private final MajorRepository majorRepository;
    private final MessageSource messageSource;
    private final PersonRepository personRepository;
    public CourseServiceImpl(CourseRepository courseRepository, MajorRepository majorRepository, MessageSource messageSource, PersonRepository personRepository) {
        super(courseRepository,messageSource);
        this.courseRepository = courseRepository;
        this.majorRepository = majorRepository;
        this.messageSource = messageSource;
        this.personRepository = personRepository;
    }


    @Override
    protected void prePersist(Course entity) {
        Major major = majorRepository.findByName(entity.getMajor().getName())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("ENTITY_NOT_FOUND",new Object[]{entity.getMajor().getName()}, LocaleContextHolder.getLocale())));

        if (courseRepository.existsByMajorAndName(major, entity.getName())) {
            throw new DuplicateEntityException(messageSource.getMessage("course.duplicate",new Object[]{entity.getName()}, LocaleContextHolder.getLocale()));
        }
        entity.setActive(true);
        entity.setMajor(major);
    }

    @Override
    protected void preUpdate(Course entity) {
        Major major = majorRepository.findByName(entity.getMajor().getName())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("ENTITY_NOT_FOUND",new Object[]{entity.getMajor().getName()}, LocaleContextHolder.getLocale())));

        if (courseRepository.existsByMajorAndName(major, entity.getName())) {
            throw new DuplicateEntityException(messageSource.getMessage("course.duplicate",new Object[]{entity.getName()}, LocaleContextHolder.getLocale()));
        }
        entity.setActive(true);
        entity.setMajor(major);
    }

    @Override
    public void deleteById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("ENTITY_NOT_FOUND",new Object[]{id}, LocaleContextHolder.getLocale())));
        course.setActive(false);
        courseRepository.save(course);
    }


    @Override
    public List<Course> findAll() {
        return courseRepository.findCourseByActiveIsTrue();
    }

    @Override
    public List<Course> findAllMajorCourse(Long MajorId) {
        return courseRepository.findCourseByMajor_Id(MajorId);
    }

    @Override
    public List<Course> findAllCourseByMajorName(String majorName) {
        return courseRepository.findCourseByMajor_Name(majorName);
    }

    @Override
    public List<Person> findAllTeacherByMajorName(String majorName) {
        return personRepository.findAllByMajor_Name(majorName);
    }
}
