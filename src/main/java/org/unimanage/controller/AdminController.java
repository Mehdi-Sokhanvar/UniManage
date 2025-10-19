package org.unimanage.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unimanage.domain.user.Person;
import org.unimanage.domain.user.Role;
import org.unimanage.service.AuthService;
import org.unimanage.util.dto.AccountResponse;
import org.unimanage.util.dto.AddRoleRequest;
import org.unimanage.util.dto.ApiResponse;
import org.unimanage.util.dto.PersonRegisterDto;
import org.unimanage.util.dto.mapper.PersonMapper;

import java.time.Instant;
import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "Endpoints for managing admin features")
public class AdminController {

    private final PersonMapper personMapper;
    private final AuthService authService;
    private final MessageSource messageSource;


    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping("/register/teacher")
    public ResponseEntity<AccountResponse> registerTeacher(@RequestBody PersonRegisterDto accountDto, Locale locale) {
        return registerPerson(accountDto, "TEACHER", locale);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/manager")
    public ResponseEntity<AccountResponse> registerManager(@RequestBody PersonRegisterDto accountDto, Locale locale) {
        return registerPerson(accountDto, "MANAGER", locale);
    }

    private ResponseEntity<AccountResponse> registerPerson(PersonRegisterDto accountDto, String role, Locale locale) {
        Person entity = personMapper.toEntity(accountDto);
        Person persist = authService.persist(entity);
        authService.addRoleToAccount(role, persist.getAccount().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                AccountResponse.builder()
                        .username(entity.getNationalCode())
                        .major(accountDto.getMajorName())
                        .build()
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/{personId}/roles")
    public ResponseEntity<String> addRoleToPerson(@PathVariable Long personId, @Valid @RequestBody AddRoleRequest request, Locale locale) {
        authService.addRoleToAccount(request.getRoleName(), personId);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                messageSource.getMessage("success.add.role", new Object[]{request.getRoleName()}, locale)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/active/account/{accountId}")
    public ResponseEntity<String> activeAccount(@PathVariable Long accountId, Locale locale) {
        authService.activeAccount(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(
                messageSource.getMessage("success.active.account", new Object[]{accountId}, locale)
        );
    }

}



