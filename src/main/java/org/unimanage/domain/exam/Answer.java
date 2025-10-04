package org.unimanage.domain.exam;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor


@Entity
@Table(name = "answer")
public class Answer extends BaseModel<Long> {

    @ManyToOne
    private ExamQuestion assessmentQuestion;

    @ManyToOne
    private ExamInstance examInstance;
}
