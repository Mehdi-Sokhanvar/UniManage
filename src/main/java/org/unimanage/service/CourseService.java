package org.unimanage.service;

import org.apache.logging.log4j.simple.internal.SimpleProvider;
import org.unimanage.domain.course.Course;
import org.unimanage.domain.user.Person;

import java.util.List;

public interface CourseService extends BaseService<Course,Long> {
    List<Course> findAllMajorCourse(Long MajorId);

    List<Course> findAllCourseByMajorName(String majorName);

    List<Person> findAllTeacherByMajorName(String majorName);
}
