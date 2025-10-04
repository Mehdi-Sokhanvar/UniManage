package org.unimanage.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unimanage.domain.course.Major;
import org.unimanage.service.MajorService;
import org.unimanage.util.dto.CourseDto;
import org.unimanage.util.dto.MajorDto;
import org.unimanage.util.dto.mapper.CourseMapper;
import org.unimanage.util.dto.mapper.MajorMapper;

import java.util.List;

@RestController
@RequestMapping("/api/majors")
@RequiredArgsConstructor
@Tag(name = "Major Management", description = "Endpoints for managing academic majors")
public class MajorController {

    private final MajorService majorService;
    private final MajorMapper majorMapper;
    private final CourseMapper courseMapper;

    private static final String ADMIN_OR_MANAGER = "hasRole('ADMIN') OR hasRole('MANAGER')";


    @PreAuthorize(ADMIN_OR_MANAGER)
    @PostMapping
    @Operation(summary = "Create a new major", description = "Creates a new academic major")
    public ResponseEntity<MajorDto> create(@RequestBody MajorDto majorDto) {
        Major persist = majorService.persist(majorMapper.toEntity(majorDto));
        return ResponseEntity.ok(majorMapper.toDTO(persist));
    }

    @PreAuthorize(ADMIN_OR_MANAGER)
    @PutMapping
    @Operation(summary = "Update a major", description = "Updates an existing academic major")
    public ResponseEntity<MajorDto> update(@RequestBody MajorDto majorDto) {
        Major persist = majorService.persist(majorMapper.toEntity(majorDto));
        return ResponseEntity.ok(majorMapper.toDTO(persist));
    }

    @PreAuthorize(ADMIN_OR_MANAGER)
    @GetMapping("/{id}")
    @Operation(summary = "Get major by ID", description = "Retrieves a specific major by its ID")
    public ResponseEntity<MajorDto> getMajor(@PathVariable Long id) {
        return ResponseEntity.ok(
                majorMapper.toDTO(majorService.findById(id))
        );
    }

    @PreAuthorize(ADMIN_OR_MANAGER)
    @GetMapping("/course/{id}")
    @Operation(summary = "Get major course", description = "Retrieves all of the course major")
    public ResponseEntity<List<CourseDto>> getMajorCourse(@PathVariable Long id) {
        return ResponseEntity.ok(
                majorService.getCoursesByMajor(id).stream()
                        .map(courseMapper::toDTO)
                        .toList()
        );
    }

    @PreAuthorize(ADMIN_OR_MANAGER)
    @GetMapping
    @Operation(summary = "Get all majors", description = "Retrieves all of the majors ")
    public ResponseEntity<List<MajorDto>> findAll() {
        return ResponseEntity.ok(
                majorService.findAll().stream()
                        .map(majorMapper::toDTO)
                        .toList());
    }

    @PreAuthorize(ADMIN_OR_MANAGER)
    @DeleteMapping("/{id}")
    public ResponseEntity<MajorDto> delete(@PathVariable Long id) {
        majorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
