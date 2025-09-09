package org.unimanage.domain.exam;

import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.util.enumration.ExamStatus;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity

public class ExamTemplate extends BaseModel<Long> {

    private Instant startTime;

    private Instant endTime;

    private Double score;
//
    private Double averageScore;

    private Double passingScore;

    @Enumerated(EnumType.STRING)
    private ExamStatus status;

    @OneToMany(mappedBy = "exam" )
    private List<ExamQuestion> examQuestions;
}
