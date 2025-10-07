package org.unimanage.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.domain.user.Role;
import org.unimanage.service.AuthService;
import org.unimanage.util.dto.*;
import org.unimanage.util.dto.mapper.PersonMapper;

import java.time.Instant;
import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Management", description = "Endpoints for managing register,login")
public class AuthController {

    private final PersonMapper personMapper;
    private final AuthService authService;
    private final MessageSource messageSource;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AccountResponse>> registerStudent(@RequestBody PersonRegisterDto accountDto, Locale locale) {
        Person persistPerson = personMapper.toEntity(accountDto);
        authService.registerPerson(persistPerson,"STUDENT");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<AccountResponse>builder()
                        .success(true)
                        .message(messageSource.getMessage("student.register.success", new Object[]{persistPerson.getFirstName(), persistPerson.getMajor().getName()}, locale))
                        .data(AccountResponse.builder()
                                .username(persistPerson.getNationalCode())
                                .major(accountDto.getMajorName())
                                .build())
                        .timestamp(Instant.now().toString())
                        .build()
        );
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@RequestBody AccountRequestDto request, Locale locale) {
        AuthResponseDto login = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<AuthResponseDto>builder()
                        .success(true)
                        .message(messageSource.getMessage("user.login.success", new Object[]{request.getUsername()}, locale))
                        .data(login)
                        .timestamp(Instant.now().toString())
                        .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader(value = "Authorization", required = false) String authorizeRequest, Locale locale) {
        String token = null;
        if (authorizeRequest != null && authorizeRequest.startsWith("Bearer ")) {
            token = authorizeRequest.substring(7);
        }
        authService.logOut(token);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<String>builder()
                        .success(true)
                        .timestamp(Instant.now().toString())
                        .message(messageSource.getMessage("user.logout.success", new Object[]{token}, locale))
                        .build()
        );
    }


    // HttpServletRequest





}
