package org.unimanage.domain.course;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.exam.ExamTemplate;
import org.unimanage.domain.user.Student;
import org.unimanage.domain.user.Teacher;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity
@Table
public class OfferedCourse extends BaseModel<Long> {

    @ManyToOne
    private Course course;

    private Instant classDate;//fixme : time

    private Instant finishedDate;//fixme : time

    private Byte capacity;  // todo : 50- 1

    private String classLocation;

    @ManyToOne
    private Teacher teacher;

    @ManyToMany
    private List<Student> students;

    @ManyToOne
    private Term terms;

    private List<ExamTemplate> examTemplates;

}
