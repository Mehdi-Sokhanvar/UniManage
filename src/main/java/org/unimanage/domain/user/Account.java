package org.unimanage.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.unimanage.domain.BaseModel;
import org.unimanage.enumration.UserStatus;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity
public class Account extends BaseModel<Long> {
    private String username;

    private String password;


    private String email;


    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToOne
    private Person person;

    private UUID authId;

}
