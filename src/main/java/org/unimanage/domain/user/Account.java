package org.unimanage.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.util.enumration.UserStatus;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Setter

@Entity
public class Account extends BaseModel<Long> {

    private String username;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Person person;

    private UUID authId;

    @OneToOne
    private Role activeRole;

}
// شماره دانشجویی ,