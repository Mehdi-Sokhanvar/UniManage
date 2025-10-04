package org.unimanage.domain.course;

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
@Table(name = "course")

public class Course extends BaseModel<Long> {

    private String name;

    private Boolean active;

    @ManyToOne
    private Major major;


    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<OfferedCourse> offeredCourses;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Question> questions;

}
