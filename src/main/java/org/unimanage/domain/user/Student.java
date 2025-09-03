package org.unimanage.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.course.OfferedCourse;
import org.unimanage.domain.exam.ExamInstance;

import java.util.List;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity
@Table
public class Student extends Person {
    @ManyToMany
    private List<OfferedCourse> offeredCourseList;

    @OneToMany(mappedBy = "student")
    private List<ExamInstance> examInstanceList;
}
