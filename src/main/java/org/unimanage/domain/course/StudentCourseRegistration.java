package org.unimanage.domain.course;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.user.Person;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor


@Entity
@Table(name = "student_registration")
public class StudentCourseRegistration extends BaseModel<Long> {

    @ManyToOne
    private Person student;

    @ManyToOne
    private OfferedCourse course;

    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;

    private Double grade;

    private LocalDate registration;

}
