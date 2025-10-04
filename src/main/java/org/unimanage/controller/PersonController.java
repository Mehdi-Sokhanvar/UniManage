package org.unimanage.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unimanage.domain.user.Role;
import org.unimanage.service.AuthService;
import org.unimanage.util.dto.ApiResponse;
import org.unimanage.util.dto.ChangeRoleRequest;
import org.unimanage.util.dto.RePasswordDto;
import org.unimanage.util.dto.RoleResponse;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('STUDENT') or hasRole('MANAGER') or hasRole('ADMIN')")
@RequiredArgsConstructor
@Tag(name = "Person Management", description = "Endpoints for managing academic courses")

public class PersonController {

    private final AuthService authService;
    private final MessageSource messageSource;

    @PutMapping("/password")
    public ResponseEntity<ApiResponse<String>> changePassword(Principal principal, @RequestBody RePasswordDto request, Locale locale) {
        authService.rePassword(request.getOldPassword(), request.getNewPassword(), principal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<String>builder()
                        .success(true)
                        .message(messageSource.getMessage("success.change.password", new Object[]{principal.getName()}, locale))
                        .build());

    }


    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getPersonRoles(Principal principal, Locale locale) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<RoleResponse>>builder()
                                .success(true)
                                .message(messageSource.getMessage("list.of.userRole", null, locale))
                                .data(authService.getPersonRoles(principal).stream()
                                        .map(role -> new RoleResponse(role.getName()))
                                        .toList())
                                .build()
                );
    }


    @GetMapping("/logout")
    public  ResponseEntity<Void> logout(Principal principal, Locale locale) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT);
    }



}

