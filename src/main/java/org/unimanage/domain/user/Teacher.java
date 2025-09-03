package org.unimanage.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.course.OfferedCourse;

import java.util.List;


@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity
@Table
public class Teacher extends Person {

    private List<OfferedCourse> offeredCourses;


}
