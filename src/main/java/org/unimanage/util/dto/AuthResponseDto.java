package org.unimanage.util.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AuthResponseDto {

    private String accessToken;
    private String refreshToken;
    private String tokenType;

}
