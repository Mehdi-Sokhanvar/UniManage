package org.unimanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unimanage.domain.course.Course;
import org.unimanage.domain.course.Major;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,Long> {

//    boolean existsByMajorAndName(Major major, String courseName);

    List<Course> findCourseByMajor_Id(Long majorId);

    List<Course> findCourseByActiveIsTrue();
}
