package org.unimanage.domain.question;


import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;


import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor


@Entity
public class Option extends BaseModel<Long> {
    private String optionText;
    private Boolean isCorrect;

    @ManyToOne
    private TestQuestion testQuestion;

}
