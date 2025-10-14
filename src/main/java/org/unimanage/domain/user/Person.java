package org.unimanage.domain.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.course.Major;
import org.unimanage.domain.course.OfferedCourse;
import org.unimanage.domain.course.StudentCourseRegistration;
import org.unimanage.domain.exam.ExamInstance;
import org.unimanage.util.enumration.Degree;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor



@Entity
@Table(name = "person")
public class Person extends BaseModel<Long> {

    private String firstName;

    private String lastName;

    private String nationalCode;

    private String email;

    private String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "person_role",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<ExamInstance> examInstanceList;


    @OneToMany
    private List<StudentCourseRegistration> studentCourseRegistrations;

    @ManyToOne(fetch = FetchType.LAZY)
    private Major major;

    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER)
    private Account account;

}

