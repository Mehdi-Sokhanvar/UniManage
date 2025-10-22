package org.unimanage.domain.course;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor


@Entity
@Table(name = "accademic_calendar")

public class AcademicCalendar extends BaseModel<Long> {

    @OneToOne(mappedBy = "academicCalendar")
    private Term term;

    private LocalDate courseRegistrationStart;
    private LocalDate courseRegistrationEnd;

    private LocalDate addDropStart;
    private LocalDate addDropEnd;

    private LocalDate classesStart;
    private LocalDate classesEnd;

    private LocalDate finalExam;

}
