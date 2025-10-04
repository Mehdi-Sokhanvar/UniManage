package org.unimanage.util.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.unimanage.util.dto.ErrorResponse;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpServletRequest request,
            HttpStatus status,
            String message) {
        return ResponseEntity.status(status).body(
                ErrorResponse.builder()
                        .success(false)
                        .message(message)
                        .statusCode(status.value())
                        .errorCode(status.getReasonPhrase())
                        .path(request.getRequestURI())
                        .timestamp(Instant.now().toString())
                        .build()
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> errorResponse(HttpServletRequest request, Exception exception) {
        return buildErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFoundException(HttpServletRequest request, EntityNotFoundException e) {
        return buildErrorResponse(request, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ErrorResponse> duplicateMajorNameException(HttpServletRequest request, DuplicateEntityException e) {
        return buildErrorResponse(request, HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> usernameNotFoundException(HttpServletRequest request, UsernameNotFoundException e) {
        return buildErrorResponse(request, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return buildErrorResponse(request, HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityExistsException(HttpServletRequest request, EntityExistsException e) {
        return buildErrorResponse(request, HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialsException(HttpServletRequest request, BadCredentialsException e) {
        return buildErrorResponse(request, HttpStatus.UNAUTHORIZED, e.getMessage());
    }


    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> authorizationDeniedException(HttpServletRequest request, AuthorizationDeniedException e) {
        return buildErrorResponse(request, HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(TimeProblemException.class)
    public ResponseEntity<ErrorResponse> timeProblemException(HttpServletRequest request, TimeProblemException e) {
        return buildErrorResponse(request, HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }


}
