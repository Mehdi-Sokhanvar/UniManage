package org.unimanage.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.unimanage.domain.course.Term;
import org.unimanage.service.TermService;
import org.unimanage.util.dto.ApiResponse;
import org.unimanage.util.dto.TermDto;
import org.unimanage.util.dto.mapper.TermMapper;

import javax.swing.plaf.PanelUI;
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
    private static final String STUDENT_OR_MANAGER = "hasRole('STUDENT') OR hasRole('MANAGER')";


    @PreAuthorize(ADMIN_OR_MANAGER)
    @PostMapping
    public ResponseEntity<TermDto> createTerm(@Valid @RequestBody TermDto termDto) {
        Term entity = termMapper.toEntity(termDto);
        Term persist = termService.persist(entity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(termMapper.toDTO(persist));
    }


    @PreAuthorize(ADMIN_OR_MANAGER)
    @PutMapping("/{id}")
    public ResponseEntity<TermDto> updateTerm(@PathVariable Long id, @Valid @RequestBody TermDto termDto) {
        Term entity = termMapper.toEntity(termDto);
        entity.setId(id);
        Term persist = termService.persist(entity);
        return ResponseEntity.status(HttpStatus.OK)
                .body(termMapper.toDTO(persist));
    }


    @PreAuthorize(ADMIN_OR_MANAGER)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerm(@PathVariable Long id) {
        termService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize(ADMIN_OR_MANAGER)
    @GetMapping
    public ResponseEntity<List<TermDto>> getAllTerms() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(termService.findAll().stream()
                        .map(termMapper::toDTO)
                        .collect(Collectors.toList()));
    }

    @PreAuthorize(STUDENT_OR_MANAGER)
    @GetMapping("/student")
    public ResponseEntity<List<TermDto>> getStudentTerms(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(termService.getAllTerms(authentication.getName()).stream()
                        .map(termMapper::toDTO)
                        .collect(Collectors.toList()));
    }


}

