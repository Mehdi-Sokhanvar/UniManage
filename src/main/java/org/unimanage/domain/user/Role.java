package org.unimanage.domain.user;


import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder

@Entity
public class Role extends BaseModel<Long> {

    private String roleName;

    @ManyToMany(mappedBy = "roles")

    private List<Person> persons;
}
