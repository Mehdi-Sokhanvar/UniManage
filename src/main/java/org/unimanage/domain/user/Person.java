package org.unimanage.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.course.OfferedCourse;
import org.unimanage.domain.exam.ExamInstance;
import org.unimanage.util.enumration.Degree;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Builder

// fixme : choose best inheritance type for class
@Entity
public class Person extends BaseModel<Long> {

    private String firstName;

    private String lastName;

    private String nationalCode;

    private String studentNumber;

    @ManyToMany(mappedBy = "persons",fetch = FetchType.EAGER)
    private List<Role> roles;

    @OneToMany(mappedBy = "student")
    private List<ExamInstance> examInstanceList;

    @ManyToMany
    private List<OfferedCourse> offeredCourseList;


    @Enumerated(EnumType.STRING)
    private Degree degree;
}

