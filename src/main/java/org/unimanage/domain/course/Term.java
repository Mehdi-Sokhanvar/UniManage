package org.unimanage.domain.course;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;
import org.unimanage.util.enumration.TermStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor



@Entity
@Table(name = "term")

public class Term extends BaseModel<Long> {

    private final String TERM_YEAR="term-year";

    @Enumerated(EnumType.STRING)
    private Semester semester;


    @Column(name = TERM_YEAR)
    private int year;

    @ManyToOne
    private Major major;

    @OneToMany(mappedBy = "term" ,cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private List<OfferedCourse>  offeredCourses;

    @OneToOne(cascade = CascadeType.ALL)
    private AcademicCalendar academicCalendar;
}
