package org.unimanage.domain.exam;

import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Answer extends BaseModel<Long> {

    @ManyToOne
    private ExamQuestion assessmentQuestion;

    @ManyToOne
    private ExamInstance examInstance;
}
