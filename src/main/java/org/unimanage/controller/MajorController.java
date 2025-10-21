package org.unimanage.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unimanage.domain.course.Major;
import org.unimanage.service.MajorService;
import org.unimanage.util.dto.ApiResponse;
import org.unimanage.util.dto.CourseDto;
import org.unimanage.util.dto.MajorDto;
import org.unimanage.util.dto.mapper.CourseMapper;
import org.unimanage.util.dto.mapper.MajorMapper;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/majors")
@RequiredArgsConstructor
@Tag(name = "Major Management", description = "Endpoints for managing academic majors")
public class MajorController {

    private final MajorService majorService;
    private final MajorMapper majorMapper;
    private final CourseMapper courseMapper;
    private final MessageSource messageSource;

    private static final String ADMIN = "hasRole('ADMIN')";


    @PreAuthorize(ADMIN)
    @PostMapping
    public ResponseEntity<MajorDto> create(@Valid @RequestBody MajorDto majorDto) {
        Major persist = majorService.persist(majorMapper.toEntity(majorDto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(majorMapper.toDTO(persist));
    }

    @PreAuthorize(ADMIN)
    @PutMapping("{id}")
    public ResponseEntity<MajorDto> update(@PathVariable Long id, @Valid  @RequestBody MajorDto majorDto) {
        majorDto.setId(id);
        Major persist = majorService.persist(majorMapper.toEntity(majorDto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(majorMapper.toDTO(persist));
    }

    @PreAuthorize(ADMIN)
    @GetMapping("/{id}")
    public ResponseEntity<MajorDto> getMajor(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(majorMapper.toDTO(majorService.findById(id)));
    }

    @PreAuthorize(ADMIN)
    @GetMapping("/course/{id}")
    public ResponseEntity<List<CourseDto>> getMajorCourse(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(majorService.getCoursesByMajor(id).stream()
                        .map(courseMapper::toDTO)
                        .toList());
    }


    @GetMapping
    public ResponseEntity<List<MajorDto>> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(majorService.findAll().stream()
                                .map(majorMapper::toDTO)
                                .toList());
    }

    @PreAuthorize(ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        majorService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
