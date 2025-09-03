package org.unimanage.domain.course;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.unimanage.domain.BaseModel;
import org.unimanage.domain.user.GroupManager;

import java.util.List;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)


@Entity
@Table
public class Major extends BaseModel<Long> {

    private String majorName;

//    fixme : choose uni or bi
    @OneToOne
    private GroupManager groupManager;

    @OneToMany(mappedBy = "major")
    private List<Course> courses;
}
