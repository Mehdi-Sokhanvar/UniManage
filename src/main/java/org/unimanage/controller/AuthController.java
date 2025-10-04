package org.unimanage.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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





}
