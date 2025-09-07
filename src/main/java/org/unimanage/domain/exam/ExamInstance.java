package org.unimanage.domain.exam;

import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.user.Person;
import org.unimanage.enumration.ExamStatus;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity

public class ExamInstance extends BaseModel<Long> {


    private Instant startTime;

    private Instant endTime;

    @Enumerated(EnumType.STRING)
    private ExamStatus examStatus;

    @ManyToOne
    private Person student;

    private Double score;

    @OneToMany(mappedBy = "examInstance",cascade = CascadeType.ALL)
    private List<Answer> answers;

//    fixme : how to handle course question

}
