package org.unimanage.domain.exam;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.question.Question;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor



@Entity
@Table(name = "exam_question")


public class ExamQuestion extends BaseModel<Long> {


    @ManyToOne
    private Question question;

    @ManyToOne
    private ExamTemplate exam;


    private Float score;

    @OneToMany(cascade = CascadeType.PERSIST,mappedBy = "assessmentQuestion")
    private List<Answer> answer;

}
