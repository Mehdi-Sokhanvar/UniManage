package org.unimanage.util.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.unimanage.domain.course.Major;
import org.unimanage.domain.course.TermType;
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
    private TermType termType;
    private int year;
    private LocalDate startTime;
    private LocalDate endTime;
    private TermStatus termStatus;
    private String majorName;

}
