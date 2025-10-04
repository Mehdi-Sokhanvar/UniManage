package org.unimanage.domain.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unimanage.domain.BaseModel;
import org.unimanage.util.enumration.AccountStatus;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor


@Entity
@Table(name = "account")
public class Account extends BaseModel<Long> {

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Person person;

    private UUID authId;

//    @ManyToOne
//    private Role activeRole;

}
