package org.unimanage.domain.exam;

import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.question.Option;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity
@Table
public class TestAnswer extends Answer{

    @ManyToOne
    private Option option;

}
