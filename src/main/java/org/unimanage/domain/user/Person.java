package org.unimanage.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.course.Major;
import org.unimanage.domain.course.OfferedCourse;
import org.unimanage.domain.exam.ExamInstance;
import org.unimanage.util.enumration.Degree;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Builder

// fixme : choose best inheritance type for class
@Entity
public class Person extends BaseModel<Long> {

    private String firstName;

    private String lastName;

    private String nationalCode;

    private String personalCode;

    private String phoneNumber;

    @ManyToMany
    @JoinTable(
            name = "person_role",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "student")
    private List<ExamInstance> examInstanceList;

    @ManyToMany
    private List<OfferedCourse> offeredCourseList;

    @ManyToOne
    private Major major;

    @OneToOne
    private Account account;
}

