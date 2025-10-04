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
public class TestQuestion extends Question {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,mappedBy = "testQuestion")
    private List<Option> options;

}
