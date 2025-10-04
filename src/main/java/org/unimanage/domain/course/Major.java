package org.unimanage.domain.course;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.user.Person;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor


@Entity
@Table(name = "major")

public class Major extends BaseModel<Long> {

    private String name;

    private UUID Code;

    private Byte numberOfUnits;

    private Boolean active;

    @OneToMany(mappedBy = "major")
    private List<Term> terms;

    @OneToMany(mappedBy = "major")
    private List<Course> courses;


    @OneToMany(mappedBy = "major",fetch = FetchType.LAZY)
    private List<Person> persons;
}
