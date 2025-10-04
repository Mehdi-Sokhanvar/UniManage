package org.unimanage.domain.exam;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;




@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
public class DescriptiveAnswer extends Answer {
}
