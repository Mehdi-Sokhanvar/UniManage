package org.unimanage.domain.user;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)


@Entity
@Table(name = "role")
public class Role extends BaseModel<Long> {

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<Person> persons=new HashSet<>();

}
