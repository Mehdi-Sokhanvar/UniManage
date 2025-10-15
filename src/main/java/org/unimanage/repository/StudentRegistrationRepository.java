package org.unimanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unimanage.domain.course.CourseStatus;
import org.unimanage.domain.course.OfferedCourse;
import org.unimanage.domain.course.StudentCourseRegistration;
import org.unimanage.util.dto.CourseRegistrationDTO;

import java.util.List;

@Repository
public interface StudentRegistrationRepository extends JpaRepository<StudentCourseRegistration, Long> {
    boolean existsByStudentIdAndCourseIdAndCourseStatusIn(Long studentId, Long courseId, List<CourseStatus> status);


    List<StudentCourseRegistration> findByCourse_Term_Id_AndStudent_Id(Long termId, Long studentId);


    @Query("""
                 SELECT new org.unimanage.util.dto.CourseRegistrationDTO(
                        oc.course.name,
                        oc.startTime,
                        oc.endTime,
                        CONCAT(t.firstName, " ",t.lastName),
                        scr.courseStatus,
                        oc.dayOfWeek,
                        scr.grade)
                    FROM StudentCourseRegistration scr
                         JOIN scr.course oc
                         JOIN oc.teacher t
                         JOIN oc.term term
                    WHERE scr.student.id = :studentId AND term.id = :termId
            """)
    List<CourseRegistrationDTO> findAllStudentCourse(
            @Param("studentId") Long studentId,
            @Param("termId") Long termId);


}
