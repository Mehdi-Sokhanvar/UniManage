package org.unimanage.domain.question;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.exam.TestAnswer;

import java.util.List;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor


@Entity
@Table
public class Option extends BaseModel<Long> {
    private String optionText;
    private Boolean isCorrect;

    @OneToMany
    private List<TestAnswer> testAnswers;
}
