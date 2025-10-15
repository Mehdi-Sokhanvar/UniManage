package org.unimanage.service;

import org.unimanage.domain.course.Course;
import org.unimanage.domain.course.OfferedCourse;
import org.unimanage.domain.course.StudentCourseRegistration;
import org.unimanage.domain.course.Term;
import org.unimanage.util.dto.CourseRegistrationDTO;
import org.unimanage.util.dto.StudentCourse;
import org.unimanage.util.exception.EntityNotFoundException;

import java.security.Principal;
import java.util.List;

public interface CourseOfferingService extends BaseService<OfferedCourse, Long> {

    List<OfferedCourse> findOfferedCoursesByTermId(Long termId);


    void getStudentCourse(Long courseId);
    void deleteStudentCourse(Long courseId);
    List<CourseRegistrationDTO> findAllCourseByTermId(Long termId);

    List<OfferedCourse> getTeacherScheduleTime(Long termId);
    List<StudentCourse> getAllStudentsByOfferedCourseId(Long offeredCourseId);

}
