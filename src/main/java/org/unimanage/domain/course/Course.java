package org.unimanage.domain.course;

import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.question.Question;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity

public class Course extends BaseModel<Long> {

    private String courseName;

    private Byte unit;

    @ManyToOne
    private Major major;

    @OneToMany(mappedBy = "course")
    private List<OfferedCourse> offeredCourses;


    @OneToMany(mappedBy = "course")
    private List<Question> questions;

}
