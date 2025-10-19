package org.unimanage.util.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RePasswordDto{
    @NotBlank(message = "Old password must not be blank")
    @Size(min = 8, message = "Old password must be at least 8 characters")
    private String oldPassword;

    @NotBlank(message = "New password must not be blank")
    @Size(min = 8, message = "New password must be at least 8 characters")
    private String newPassword;
}
