package org.unimanage.domain.exam;

import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.question.Question;

import java.util.List;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity
@Table
public class ExamQuestion extends BaseModel<Long> {

    @ManyToOne
    private Question question;

    @ManyToOne
    private ExamTemplate exam;


    private Float score;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Answer> answer;

    @OneToMany(mappedBy = "assessmentQuestion")
    private List<Answer> answers;
}
