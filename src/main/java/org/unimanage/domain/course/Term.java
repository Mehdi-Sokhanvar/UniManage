package org.unimanage.domain.course;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;
import org.unimanage.util.enumration.TermStatus;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor



@Entity
@Table(name = "term")

public class Term extends BaseModel<Long> {

    private Byte termType;
    private int year;
    private Instant startTime;
    private Instant endTime;

    @Enumerated(EnumType.STRING)
    private TermStatus termStatus;

    @ManyToOne
    private Major major;

    @OneToMany(mappedBy = "term" ,cascade = CascadeType.REMOVE)
    private List<OfferedCourse>  offeredCourses;
}
