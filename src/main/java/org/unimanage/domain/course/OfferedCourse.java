package org.unimanage.domain.course;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.exam.ExamTemplate;
import org.unimanage.domain.user.Enrollment;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity

public class OfferedCourse extends BaseModel<Long> {

    @ManyToOne
    private Course course;

    private Instant classDate;//fixme : time

    private Instant finishedDate;//fixme : time

    private Byte capacity;  // todo : 50- 1

    private String classLocation;


    @OneToMany(mappedBy = "offeredCourse")
    private List<Enrollment> enrollmentList;

    @ManyToOne
    private Term term;

    private List<ExamTemplate> examTemplates;

}
