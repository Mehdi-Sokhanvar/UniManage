package org.unimanage.domain.course;

import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.exam.ExamTemplate;
import org.unimanage.domain.user.Person;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity

public class OfferedCourse extends BaseModel<Long> {

    @ManyToOne
    private Course course;

    private Instant classDate;//fixme : time

    private Instant finishedDate;//fixme : time

    private Byte capacity;  // todo : 50- 1

    private String classLocation;

    private Byte semester;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Person teacher;

    @ManyToMany(mappedBy = "offeredCourseList")
    private List<Person> studentList;

    @ManyToOne
    private Term term;

    private List<ExamTemplate> examTemplates;

}
