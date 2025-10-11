package org.unimanage.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unimanage.domain.course.Term;import org.unimanage.service.TermService;
import org.unimanage.util.dto.ApiResponse;
import org.unimanage.util.dto.TermDto;
import org.unimanage.util.dto.mapper.TermMapper;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/terms/")
@RequiredArgsConstructor
public class TermController {

    private final TermService termService;
    private final TermMapper termMapper;
    private final MessageSource messageSource;


    private static final String ADMIN_OR_MANAGER = "hasRole('ADMIN') OR hasRole('MANAGER')";
    private static final String ALL_AUTHENTICATED = "hasRole('ADMIN') OR hasRole('MANAGER') OR hasRole('STUDENT')";


    @PreAuthorize(ADMIN_OR_MANAGER)
    @PostMapping
    public ResponseEntity<ApiResponse<TermDto>> createTerm(@RequestBody TermDto termDto) {

        Term entity = termMapper.toEntity(termDto);
        Term persist = termService.persist(entity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<TermDto>builder()
                        .success(true)
                        .message(messageSource.getMessage("term.created", null, LocaleContextHolder.getLocale()))
                        .data(termMapper.toDTO(persist))
                        .timestamp(Instant.now().toString())
                        .build());
    }

    @PreAuthorize(ADMIN_OR_MANAGER)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TermDto>> updateTerm(@PathVariable Long id, @RequestBody TermDto termDto) {
        Term entity = termMapper.toEntity(termDto);
        entity.setId(id);
        Term persist = termService.persist(entity);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<TermDto>builder()
                        .success(true)
                        .message(messageSource.getMessage("term.updated", null, LocaleContextHolder.getLocale()))
                        .data(termMapper.toDTO(persist))
                        .timestamp(Instant.now().toString())
                        .build());
    }


    @PreAuthorize(ADMIN_OR_MANAGER)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<TermDto>> deleteTerm(@PathVariable Long id) {
        termService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.<TermDto>builder()
                        .success(true)
                        .message(messageSource.getMessage("term.deleted", null, LocaleContextHolder.getLocale()))
                        .data(null)
                        .timestamp(Instant.now().toString())
                        .build());
    }

    @PreAuthorize(ADMIN_OR_MANAGER)
    @GetMapping
    public ResponseEntity<ApiResponse<List<TermDto>>> getAllTerms() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<List<TermDto>>builder()
                        .success(true)
                        .message(messageSource.getMessage("term.listed", null, LocaleContextHolder.getLocale()))
                        .data(
                                termService.findAll().stream()
                                        .map(termMapper::toDTO)
                                        .collect(Collectors.toList()))
                        .timestamp(Instant.now().toString())
                        .build());
    }


}

