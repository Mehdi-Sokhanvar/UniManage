package org.unimanage.util.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PersonRequestDto {
    private String firstName;
    private String lastName;
    private String nationalCode;
    private String phoneNumber;
}
