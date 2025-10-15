package org.unimanage.util.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentCourse {
    private String firstName;
    private String lastName;
    private String nationalCode;
}
