package org.unimanage.domain.user;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;

import java.util.List;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)


@Entity
@Table(name = "role")
public class Role extends BaseModel<Long> {

    private String name;

//    @ManyToMany(mappedBy = "roles")
//    private List<Person> persons;

   @ManyToMany(mappedBy = "roles")
    private List<Account> accounts;

}
