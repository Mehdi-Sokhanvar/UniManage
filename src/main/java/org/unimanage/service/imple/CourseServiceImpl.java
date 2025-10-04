package org.unimanage.service.imple;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.unimanage.domain.course.Course;
import org.unimanage.domain.course.Major;
import org.unimanage.repository.CourseRepository;
import org.unimanage.repository.MajorRepository;
import org.unimanage.service.CourseService;
import org.unimanage.util.message.ErrorMessage;
import org.unimanage.util.exception.AccessDeniedException;
import org.unimanage.util.exception.DuplicateEntityException;

import java.util.List;

@Service
public class CourseServiceImpl extends BaseServiceImpl<Course, Long> implements CourseService {


    private final CourseRepository courseRepository;
    private final MajorRepository majorRepository;

    public CourseServiceImpl(CourseRepository courseRepository, MajorRepository majorRepository) {
        super(courseRepository);
        this.courseRepository = courseRepository;
        this.majorRepository = majorRepository;
    }


    @Override
    protected void prePersist(Course entity) {
        Long majorId = entity.getMajor().getId();
        Major major = majorRepository.findById(majorId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ENTITY_NOT_FOUND.format(entity.getMajor().getId())));
        if (!major.getActive()) {
            throw new AccessDeniedException(ErrorMessage.MAJOR_INACTIVE.format());
        }
//        if (courseRepository.existsByMajorAndCourseName(major, entity.getName())) {
//            throw new DuplicateEntityException(ErrorMessage.COURSE_ALREADY_EXISTS.format());
//        }
        entity.setActive(true);

    }

    @Override
    public void deleteById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ENTITY_NOT_FOUND.format(id)));
        course.setActive(false);
        courseRepository.save(course);
    }


    @Override
    public Course findById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ENTITY_NOT_FOUND.format(id)));
        if (course.getActive()) {
            return course;
        }
        throw new AccessDeniedException(ErrorMessage.COURSE_NOT_ACTIVE.format(course.getId()));
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findCourseByActiveIsTrue();
    }

    @Override
    public List<Course> findAllMajorCourse(Long MajorId) {
        return courseRepository.findCourseByMajor_Id(MajorId);
    }
}
