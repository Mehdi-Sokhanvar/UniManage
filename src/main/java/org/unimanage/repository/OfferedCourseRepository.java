package org.unimanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unimanage.domain.course.OfferedCourse;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface OfferedCourseRepository extends JpaRepository<OfferedCourse, Long> {

    List<OfferedCourse> findByTeacherIdAndDayOfWeek(Long teacherId, DayOfWeek dayOfWeek);
}
