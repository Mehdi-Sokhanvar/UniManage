package org.unimanage.service;

import org.unimanage.domain.course.Course;

import java.util.List;

public interface CourseService extends BaseService<Course,Long> {
    List<Course> findAllMajorCourse(Long MajorId);
}
