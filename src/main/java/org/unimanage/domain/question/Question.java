package org.unimanage.domain.question;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.course.Course;
import org.unimanage.domain.exam.ExamQuestion;

import java.util.List;


@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity
@Table
public class Question extends BaseModel<Long> {

    @ManyToOne
    private Course course;

    @OneToMany
    private List<ExamQuestion> examQuestions;
}
