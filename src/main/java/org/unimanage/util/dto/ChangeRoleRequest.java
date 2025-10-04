package org.unimanage.util.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoleRequest {

    @NotBlank(message = "Role name cannot be blank")
    @Pattern(
            regexp = "^(ADMIN|MANAGER|TEACHER|STUDENT)$",
            message = "Role must be one of: ADMIN, MANAGER, TEACHER, STUDENT"
    )
    private String roleName;
}