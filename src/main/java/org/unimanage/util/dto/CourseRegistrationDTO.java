package org.unimanage.util.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unimanage.domain.course.Course;
import org.unimanage.domain.course.CourseStatus;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder

public class CourseRegistrationDTO {

    private String courseName;
    private LocalTime startTime;
    private LocalTime endTime;
    private String teacherName;
    private CourseStatus courseStatus;
    private DayOfWeek dayOfWeek;
    private Double score;

    public CourseRegistrationDTO(String courseName, LocalTime startTime, LocalTime endTime,
                                 String teacherName, CourseStatus courseStatus, DayOfWeek dayOfWeek,
                                 Double score) {
        this.courseName = courseName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.teacherName = teacherName;
        this.courseStatus = courseStatus;
        this.dayOfWeek = dayOfWeek;
        this.score = score;
    }
}
