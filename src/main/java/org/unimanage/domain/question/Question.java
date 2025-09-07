package org.unimanage.domain.question;

import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.course.Course;
import org.unimanage.domain.exam.ExamQuestion;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Question extends BaseModel<Long> {

    @ManyToOne
    private Course course;

    @OneToMany(mappedBy = "question")
    private List<ExamQuestion> examQuestions;
}
