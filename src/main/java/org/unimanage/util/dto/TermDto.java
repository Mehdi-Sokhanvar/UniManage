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

    @NotNull
    private Semester semester;


    @NotNull
    private String majorName;


    private LocalDate courseRegistrationStart;
    private LocalDate courseRegistrationEnd;

    private LocalDate addDropStart;
    private LocalDate addDropEnd;

    private LocalDate classesStart;
    private LocalDate classesEnd;
}
