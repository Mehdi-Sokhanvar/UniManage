package org.unimanage.domain.question;

import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;

import java.util.List;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity
@Table
public class TestQuestion extends BaseModel<Long> {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Option> options;

}
