package org.unimanage.domain.course;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;
import org.unimanage.util.enumration.TermStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor



@Entity
@Table(name = "term")

public class Term extends BaseModel<Long> {

    @Enumerated(EnumType.STRING)
    private TermType termType;
    private int year;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private TermStatus termStatus;

    @ManyToOne
    private Major major;

    @OneToMany(mappedBy = "term" ,cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private List<OfferedCourse>  offeredCourses;
}
