package org.unimanage.domain.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.unimanage.domain.BaseModel;
import org.unimanage.util.enumration.AccountStatus;

import java.util.HashSet;
import java.util.Set;
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

    private UUID authId;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Person person;


}
