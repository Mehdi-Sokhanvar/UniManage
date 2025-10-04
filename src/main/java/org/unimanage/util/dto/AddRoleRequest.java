package org.unimanage.util.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Setter
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddRoleRequest {

    @NotBlank(message = "Role name cannot be blank")
    @Pattern(
            regexp = "^(ADMIN|MANAGER|TEACHER|STUDENT)$",
            message = "Role must be one of: ADMIN, MANAGER, TEACHER, STUDENT"
    )
    private String roleName;

    private String description;
}