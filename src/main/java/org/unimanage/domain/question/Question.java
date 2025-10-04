package org.unimanage.domain.question;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.course.Course;
import org.unimanage.domain.exam.ExamQuestion;

import java.util.List;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor


@Entity
@Table(name = "question")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Question extends BaseModel<Long> {

    @ManyToOne
    private Course course;

    @OneToMany(mappedBy = "question")
    private List<ExamQuestion> examQuestions;
}
