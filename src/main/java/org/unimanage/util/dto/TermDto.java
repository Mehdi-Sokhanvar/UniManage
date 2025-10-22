package org.unimanage.util.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.*;
import org.unimanage.domain.course.Major;
import org.unimanage.domain.course.Semester;
import org.unimanage.util.enumration.TermStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class TermDto {
    private Long id;

    @NotNull(message = "Semester is required")
    private Semester semester;

    @NotNull(message = "Major name is required")
    private String majorName;

    @FutureOrPresent(message = "Course registration start must be today or in the future")
    private LocalDate courseRegistrationStart;

    @FutureOrPresent(message = "Course registration end must be today or in the future")
    private LocalDate courseRegistrationEnd;

    @FutureOrPresent(message = "Add/Drop start must be today or in the future")
    private LocalDate addDropStart;

    @FutureOrPresent(message = "Add/Drop end must be today or in the future")
    private LocalDate addDropEnd;

    @FutureOrPresent(message = "Classes start must be today or in the future")
    private LocalDate classesStart;

    @FutureOrPresent(message = "Classes end must be today or in the future")
    private LocalDate classesEnd;


    private int year;

    @FutureOrPresent
    private LocalDate finalExam;

    @AssertTrue(message = "Course registration start must be before end")
    public boolean isCourseRegistrationStart() {
        return !courseRegistrationStart.isAfter(courseRegistrationEnd);
    }

    @AssertTrue(message = "Course registration start must be before end")
    public boolean isFinalExamDateBeforeEndtime() {
        return !courseRegistrationStart.isAfter(courseRegistrationEnd);
    }

    @AssertTrue(message = "Add/Drop start must be before end")
    public boolean isAddDropValid() {
        return addDropStart == null || addDropEnd == null
                || !addDropStart.isAfter(addDropEnd);
    }

    @AssertTrue(message = "Classes start must be before end")
    public boolean isClassesValid() {
        return classesStart == null || classesEnd == null
                || !classesStart.isAfter(classesEnd);
    }
}
