package org.unimanage.domain.course;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.exam.ExamTemplate;
import org.unimanage.domain.user.Person;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor


@Entity
@Table(name = "offered_course")

public class OfferedCourse extends BaseModel<Long> {

    @ManyToOne
    private Course course;

    private Instant classDate;//fixme : time

    private Instant finishedDate;//fixme : time

    private Byte capacity;  // todo : 50- 1

    private String classLocation;


    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Person teacher;

    @ManyToMany(mappedBy = "offeredCourseList", fetch = FetchType.EAGER)
    private List<Person> studentList;

    @ManyToOne
    private Term term;

    @OneToMany(mappedBy = "offeredCourse")
    private List<ExamTemplate> examTemplates;

}
