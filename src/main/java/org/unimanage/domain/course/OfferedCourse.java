package org.unimanage.domain.course;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.exam.ExamTemplate;
import org.unimanage.domain.user.Person;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
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

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer capacity;

    private String classLocation;


    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Person teacher;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "enrollments",
            joinColumns = @JoinColumn(name = "offeres_course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Person> studentList;

    @ManyToOne
    private Term term;

    @OneToMany(mappedBy = "offeredCourse", fetch = FetchType.LAZY)
    private List<ExamTemplate> examTemplates;

}
