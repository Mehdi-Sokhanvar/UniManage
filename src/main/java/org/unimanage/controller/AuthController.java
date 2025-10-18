package org.unimanage.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
@Tag(name = "Auth Controller", description = "Endpoints for managing register,login")
public class AuthController {

    private final AuthService authService;
    private final PersonMapper personMapper;
    private final MessageSource messageSource;


    @PostMapping("/register")
    public ResponseEntity<AccountResponse> registerStudent(@Valid @RequestBody PersonRegisterDto accountDto) {
        Person entity = personMapper.toEntity(accountDto);
        Person persist = authService.persist(entity);
        authService.addRoleToAccount("STUDENT", persist.getAccount().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                AccountResponse.builder()
                        .username(entity.getNationalCode())
                        .major(accountDto.getMajorName())
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AccountRequestDto request) {
        AuthResponseDto login = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(login);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization", required = false) String authorizeRequest) {
        String token = null;
        if (authorizeRequest != null && authorizeRequest.startsWith("Bearer ")) {
            token = authorizeRequest.substring(7);
        }
        authService.logOut(token);
        return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("user.logout.success", new Object[]{token}, LocaleContextHolder.getLocale()));
    }
}
