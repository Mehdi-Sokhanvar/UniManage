package org.unimanage.controller;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unimanage.domain.user.Person;
import org.unimanage.domain.user.Role;
import org.unimanage.service.AuthService;
import org.unimanage.util.dto.AccountResponse;
import org.unimanage.util.dto.PersonRequestDto;
import org.unimanage.util.dto.config.PersonMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {


    private PersonMapper personMapper;
    private final AuthService authService;

    public AuthController(PersonMapper personMapper, AuthService authService) {
        this.personMapper = personMapper;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> registerStudent(@Valid @RequestBody PersonRequestDto accountDto) {
        Person entity = personMapper.toEntity(accountDto);
        authService.registerStudent(entity);
        return ResponseEntity.ok().body(AccountResponse.builder().build());
    }

    @PostMapping("/teacher/register")
    public ResponseEntity<AccountResponse> addTeacher(@Valid @RequestBody PersonRequestDto accountDto) {
        Person entity = personMapper.toEntity(accountDto);
        authService.addTeacher(entity);
        return ResponseEntity.ok().body(AccountResponse.builder().build());
    }

}
