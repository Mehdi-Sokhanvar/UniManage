package org.unimanage.domain.exam;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.question.Option;

import java.util.List;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor


@Entity
public class TestAnswer extends Answer {


    @OneToOne(cascade = CascadeType.ALL)
    private Option option;
}
