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

    @Enumerated(EnumType.STRING)
    private Semester semester;

    private int year;

    @ManyToOne
    private Major major;

    @OneToMany(mappedBy = "term" ,cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private List<OfferedCourse>  offeredCourses;

    @OneToOne(cascade = CascadeType.ALL)
    private AcademicCalendar academicCalendar;
}
