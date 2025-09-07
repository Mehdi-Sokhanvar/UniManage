package org.unimanage.domain.course;

import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity

public class Term extends BaseModel<Long> {

    private Short year;
    private Short month;
    private String semester;
    private Byte termType;
    private Instant startTime;
    private Instant endTime;

// fixme : use this enumrated

    @OneToMany(mappedBy = "term" )
    private List<OfferedCourse>  offeredCourses;
}
