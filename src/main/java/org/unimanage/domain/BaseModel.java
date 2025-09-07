package org.unimanage.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.io.Serializable;
import java.time.Instant;



@Getter
@AllArgsConstructor
@NoArgsConstructor

@MappedSuperclass
public class BaseModel<ID extends Serializable>  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant created;

    @UpdateTimestamp
    private Instant updated;

    @Version
    private Long version;



}
