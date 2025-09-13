package org.unimanage.domain.course;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.user.Person;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity

public class Major extends BaseModel<Long> {

    private String majorName;

    private Byte semester;

    @OneToMany(mappedBy = "major")
    private List<Course> courses;

    @OneToMany(mappedBy = "major")
    private List<Person> persons;
}
