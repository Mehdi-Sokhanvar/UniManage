package org.unimanage.domain.question;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;


import java.util.List;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor



@Entity
@Table(name = "option")
public class Option extends BaseModel<Long> {
    private String optionText;
    private Boolean isCorrect;

    @ManyToOne
    private TestQuestion testQuestion;

}
