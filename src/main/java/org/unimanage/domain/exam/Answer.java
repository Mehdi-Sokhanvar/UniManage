package org.unimanage.domain.exam;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.user.Student;


@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity
@Table
public class Answer extends BaseModel<Long> {

    @ManyToOne
    private ExamQuestion assessmentQuestion;

    @ManyToOne
    private ExamInstance examInstance;
}
