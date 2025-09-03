package org.unimanage.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;

import java.util.List;


@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

// fixme : choose best inheritance type for class
@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)
public class Person extends BaseModel<Long> {

    private String firstName;

    private String lastName;

    private String nationalCode;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Account account; // todo:  به این خاطر این قسمت رو از این نوع persis در نظر گرفتیم که این مفهوم رو زمانیک ه داره یه person اضافه میشه همون موقع account هم اضفه بشه

}
