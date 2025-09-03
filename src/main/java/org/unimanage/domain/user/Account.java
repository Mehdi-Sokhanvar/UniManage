package org.unimanage.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.enumration.UserStatus;



@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity
@Table
public class Account extends BaseModel<Long> {



    private String username;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;


}
