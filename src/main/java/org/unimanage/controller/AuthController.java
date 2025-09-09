package org.unimanage.controller;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.service.AuthService;
import org.unimanage.util.dto.AccountDto;
import org.unimanage.util.dto.AccountResponse;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    private ResponseEntity<Map<String, String>> registerStudent(@Valid @RequestBody AccountDto accountDto) {
        authService.registerStudent(accountDto);
        return ResponseEntity.ok().body(Map.of("message", "Successfully registered student in major"));
    }

    @PostMapping("/registser")
    private ResponseEntity<Map<String, AccountResponse>> registerTeacher(@Valid @RequestBody AccountDto accountDto) {
//        authService.registerStudent(accountDto);
//        return ResponseEntity.ok().body(Map.of("message", "Register Successfully!",
//                );
        return ResponseEntity.ok().body(new HashMap<String, AccountResponse>());
    }

}
