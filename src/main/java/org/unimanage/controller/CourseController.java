package org.unimanage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unimanage.domain.course.Course;
import org.unimanage.service.CourseService;
import org.unimanage.util.dto.CourseDto;
import org.unimanage.util.dto.mapper.CourseMapper;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Course Management", description = "Endpoints for managing academic courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;

    private static final String ADMIN_OR_MANAGER = "hasRole('ADMIN') OR hasRole('MANAGER')";
    private static final String ALL_AUTHENTICATED = "hasRole('ADMIN') OR hasRole('MANAGER') OR hasRole('STUDENT')";


    @PreAuthorize(ADMIN_OR_MANAGER)
    @PostMapping
    @Operation(summary = "Create a new course", description = "Creates a new academic course. Requires ADMIN or MANAGER role.")
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto request) {
        Course createdCourse = courseService.persist(courseMapper.toEntity(request));
        URI location = URI.create("/api/v1/courses/" + createdCourse.getId());
        return ResponseEntity.created(location).body(courseMapper.toDTO(createdCourse));
    }

    @PreAuthorize(ADMIN_OR_MANAGER)
    @GetMapping
    @Operation(summary = "Get all courses", description = "Retrieves all of the major courses")
    public ResponseEntity<List<CourseDto>> getCourses() {
        return ResponseEntity.ok(
                courseService.findAll().stream()
                        .map(courseMapper::toDTO)
                        .toList()
        );
    }

    @PreAuthorize(ALL_AUTHENTICATED)
    @GetMapping("major/{majorId}")
    @Operation(summary = "Get courses by major", description = "Retrieves all courses for a specific major")
    public ResponseEntity<List<CourseDto>> getMajorCourses(@PathVariable Long majorId) {
        return ResponseEntity.ok(
                courseService.findAllMajorCourse(majorId).stream()
                        .map(courseMapper::toDTO)
                        .toList());
    }


    @PutMapping
    @PreAuthorize(ADMIN_OR_MANAGER)
    @Operation(summary = "Update a course", description = "Updates an existing course. Requires ADMIN or MANAGER role.")
    public ResponseEntity<CourseDto> updateCourse(@RequestBody CourseDto request) {
        Course persist = courseService.persist(courseMapper.toEntity(request));
        return ResponseEntity.ok(courseMapper.toDTO(persist));
    }


    @DeleteMapping("/{majorId}")
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<CourseDto> deleteCourse(@PathVariable Long majorId) {
        courseService.deleteById(majorId);
        return ResponseEntity.ok(courseMapper.toDTO(courseService.findById(majorId)));
    }



}
