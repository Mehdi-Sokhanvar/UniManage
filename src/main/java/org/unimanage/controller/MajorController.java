package org.unimanage.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    private static final String ADMIN_OR_MANAGER = "hasRole('ADMIN') OR hasRole('MANAGER')";



    @PreAuthorize(ADMIN_OR_MANAGER)
    @PostMapping
    public ResponseEntity<ApiResponse<MajorDto>> create(@RequestBody MajorDto majorDto) {
        Major persist = majorService.persist(majorMapper.toEntity(majorDto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<MajorDto>builder()
                                .success(true)
                                .message(messageSource.getMessage("major.creation.success", null, LocaleContextHolder.getLocale()))
                                .data(majorMapper.toDTO(persist))
                                .timestamp(Instant.now().toString())
                                .build()
                );
    }

    @PreAuthorize(ADMIN_OR_MANAGER)
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<MajorDto>> update( @PathVariable Long id,@RequestBody MajorDto majorDto) {
        majorDto.setId(id);
        Major persist = majorService.persist(majorMapper.toEntity(majorDto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<MajorDto>builder()
                                .success(true)
                                .message(messageSource.getMessage("major.update.success", null, LocaleContextHolder.getLocale()))
                                .data(majorMapper.toDTO(persist))
                                .timestamp(Instant.now().toString())
                                .build()
                );
    }

    @PreAuthorize(ADMIN_OR_MANAGER)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MajorDto>> getMajor(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<MajorDto>builder()
                                .success(true)
                                .message(messageSource.getMessage("major.get.success", null, LocaleContextHolder.getLocale()))
                                .data(majorMapper.toDTO(majorService.findById(id)))
                                .timestamp(Instant.now().toString())
                                .build()
                );
    }

    @PreAuthorize(ADMIN_OR_MANAGER)
    @GetMapping("/course/{id}")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getMajorCourse(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<CourseDto>>builder()
                                .success(true)
                                .message(messageSource.getMessage("major.get.success", null, LocaleContextHolder.getLocale()))
                                .data(   majorService.getCoursesByMajor(id).stream()
                                        .map(courseMapper::toDTO)
                                        .toList())
                                .timestamp(Instant.now().toString())
                                .build()
                );
    }

    @PreAuthorize(ADMIN_OR_MANAGER)
    @GetMapping
    public ResponseEntity<ApiResponse<List<MajorDto>>> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<MajorDto>>builder()
                                .success(true)
                                .message(messageSource.getMessage("major.get.success", null, LocaleContextHolder.getLocale()))
                                .timestamp(Instant.now().toString())
                                .data(  majorService.findAll().stream()
                                        .map(majorMapper::toDTO)
                                        .toList())
                                .build()
                );
    }

    @PreAuthorize(ADMIN_OR_MANAGER)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        majorService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
