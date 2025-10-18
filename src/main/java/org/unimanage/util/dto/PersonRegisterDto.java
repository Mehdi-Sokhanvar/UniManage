package org.unimanage.util.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Data
@Builder
public class PersonRegisterDto {

    @NotNull(message = "First name cannot be null")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    private String lastName;

    @NotNull(message = "National code cannot be null")
    @Pattern(regexp = "^[0-9]{10}$", message = "National code must be exactly 10 digits")
    private String nationalCode;

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be between 10 and 15 digits, and may start with '+'")
    private String phoneNumber;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Major name cannot be null")
    @Size(min = 1, max = 100, message = "Major name must be between 1 and 100 characters")
    private String majorName;

}

