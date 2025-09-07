package org.unimanage.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.exam.ExamInstance;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter

// fixme : choose best inheritance type for class
@Entity

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Person extends BaseModel<Long> {

    private String firstName;

    private String lastName;

    private String nationalCode;

    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "personList")
    private List<Role> roles;

    @OneToMany(mappedBy = "student")
    private List<ExamInstance> examInstanceList;

    @Transient
    private String activeRole;


    @OneToMany(mappedBy = "person")
    private List<Enrollment> offeredCourseList;


}

