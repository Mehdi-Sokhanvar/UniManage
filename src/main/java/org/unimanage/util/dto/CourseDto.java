package org.unimanage.util.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CourseDto {

    private Long id;

    // Validate that 'name' is not blank and has a size between 2 and 100 characters
    @NotBlank(message = "Course name cannot be blank")
    @Size(min = 2, max = 100, message = "Course name must be between 2 and 100 characters")
    private String name;

    // Validate that 'majorName' is not blank and has a size between 2 and 100 characters
    @NotBlank(message = "Major name cannot be blank")
    @Size(min = 2, max = 100, message = "Major name must be between 2 and 100 characters")
    private String majorName;

}
