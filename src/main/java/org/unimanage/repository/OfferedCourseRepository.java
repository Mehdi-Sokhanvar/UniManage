package org.unimanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unimanage.domain.course.OfferedCourse;
import org.unimanage.domain.course.Term;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface OfferedCourseRepository extends JpaRepository<OfferedCourse, Long> {

    List<OfferedCourse> findByTeacherIdAndDayOfWeek(Long teacherId, DayOfWeek dayOfWeek);


    @Query("""
            SELECT EXISTS (
                        SELECT 1 
                        FROM OfferedCourse o
                         WHERE
                           o.teacher.id = :teacherId  AND
                           o.term.id = :termId        AND 
                           o.dayOfWeek = :daysOfWeek  AND 
                           o.startTime <= :startTime  AND
                           o.endTime >= :endTime)
            """)
    boolean isTeacherOverlappingSchedule(@Param("teacherId") Long teacherId,
                                 @Param("termId") Long termId,
                                 @Param("daysOfWeek") DayOfWeek day,
                                 @Param("startTime") LocalTime startTime,
                                 @Param("endTime") LocalTime endTime);

    List<OfferedCourse> findByTermId(Long termId);



    List<OfferedCourse> findAllByTeacher_IdAndTerm_Id(Long teacherId, Long termId);
}
