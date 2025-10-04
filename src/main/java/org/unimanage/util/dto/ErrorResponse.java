package org.unimanage.util.dto;

import lombok.*;


@Getter
@Setter
@Builder
public class ErrorResponse {
    private boolean success;
    private String message;
    private String errorCode;
    private int statusCode;
    private String path;
    private String timestamp;


}
