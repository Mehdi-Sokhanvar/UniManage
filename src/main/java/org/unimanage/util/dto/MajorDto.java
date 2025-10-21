package org.unimanage.util.dto;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class MajorDto {

    @Positive(message = "ID must be a positive number")
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @Min(value = 1, message = "Number of units must be at least 1")
    @Max(value = 100, message = "Number of units cannot exceed 100")
    private byte numberOfUnits;
}
