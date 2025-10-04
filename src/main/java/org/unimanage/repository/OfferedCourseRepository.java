package org.unimanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unimanage.domain.course.OfferedCourse;

@Repository
public interface OfferedCourseRepository extends JpaRepository<OfferedCourse, Long> {
}
