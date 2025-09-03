package org.unimanage.domain.user;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
public class Role extends BaseModel<Long> {

    private String roleName;

    @ManyToMany
    private List<Person> personList;
}
