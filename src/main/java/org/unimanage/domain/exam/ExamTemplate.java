package org.unimanage.domain.exam;

import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.enumration.ExamStatus;

import java.time.Instant;
import java.util.List;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity
@Table
public class ExamTemplate extends BaseModel<Long> {

    private Instant startTime;

    private Instant endTime;

    private Double score;
//
    private Double averageScore;

    private Double passingScore;

    @Enumerated(EnumType.STRING)
    private ExamStatus status;

    @OneToMany
    private List<ExamQuestion> examQuestions;
}
