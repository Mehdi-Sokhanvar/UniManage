package org.unimanage.domain.log;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.unimanage.domain.BaseModel;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor


@Entity
@Table(name = "logs"
        , indexes = {
        @Index(name = "id_user_log", columnList = "user")
})
public class Logs extends BaseModel<Long> {


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogAction action;

    @Column(length = 1000)
    private String message;

    @Column(nullable = false)
    private String agent;

    private String ip;

    private LocalDateTime timeStamp;

    @Column(length = 2000)
    private String requestURI;

    @Lob
    private String oldData;

    @Lob
    private String newData;

    @Enumerated(EnumType.STRING)
    private LogStatus status;

    private String typeEntity;


}
