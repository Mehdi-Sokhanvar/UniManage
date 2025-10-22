package org.unimanage.domain.exam;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.course.OfferedCourse;
import org.unimanage.util.enumration.ExamStatus;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor


@Entity
@Table(name = "exam_template")

public class ExamTemplate extends BaseModel<Long> {

    private String title;

    private String description;

    private LocalTime startTime;

    private LocalTime endTime;

    private Double score;

    private Double averageScore;

    private Double passingScore;

    @Enumerated(EnumType.STRING)
    private ExamStatus status;

    @OneToMany(mappedBy = "exam")
    private List<ExamQuestion> examQuestions;

    @ManyToOne(fetch = FetchType.LAZY)
    private OfferedCourse offeredCourse;
}
