package org.unimanage.service;

import org.unimanage.domain.course.Course;
import org.unimanage.domain.course.Major;

import java.util.List;

public interface MajorService extends BaseService<Major,Long> {

    List<Course> getCoursesByMajor(Long majorId);
}
