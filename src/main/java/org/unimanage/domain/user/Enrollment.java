package org.unimanage.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.course.OfferedCourse;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity
public class Enrollment extends BaseModel<Long> {

    @ManyToOne
    private Person person;

    @ManyToOne
    private OfferedCourse offeredCourse;

    @OneToOne
    private Role role;


}
