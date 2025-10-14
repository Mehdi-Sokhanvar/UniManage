package org.unimanage.util.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.unimanage.util.customAnnotation.StartBeforeEndTime;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@StartBeforeEndTime
public class OfferedCourseDTO {

    @NotNull(message = "courseId is required.")
    private Long courseId;

    @NotNull(message = "termId is required.")
    private Long termId;

    @NotNull(message = "teacherId is required.")
    private Long teacherId;

    @NotNull(message = "dayOfWeek cannot be null.")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "startTime cannot be null.")
    private LocalTime startTime;

    @NotNull(message = "endTime cannot be null.")
    private LocalTime endTime;

    @NotNull(message = "capacity cannot be null.")
    @Min(value = 1, message = "capacity must be greater than zero.")
    private Integer capacity;

}
