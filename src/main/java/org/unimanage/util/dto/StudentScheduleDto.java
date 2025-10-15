package org.unimanage.util.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentScheduleDto {

    private Long courseId;
    private String courseName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer capacity;
    private String teacherName;
    private DayOfWeek dayOfWeek;

}
