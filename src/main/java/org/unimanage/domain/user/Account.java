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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    private UUID authId;



}
