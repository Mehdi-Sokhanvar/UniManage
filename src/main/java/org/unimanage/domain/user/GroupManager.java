package org.unimanage.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.unimanage.domain.BaseModel;


@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity
@Table
public class GroupManager extends Person {
}
