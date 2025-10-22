package org.unimanage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unimanage.domain.course.Course;
import org.unimanage.service.CourseService;
import org.unimanage.util.dto.ApiResponse;
import org.unimanage.util.dto.CourseDto;
import org.unimanage.util.dto.mapper.CourseMapper;

import java.net.URI;
import java.security.Principal;
import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Course Management", description = "Endpoints for managing academic courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;
    private final MessageSource messageSource;

    private static final String ADMIN_OR_MANAGER = "hasRole('ADMIN') OR hasRole('MANAGER')";
    private static final String ALL_AUTHENTICATED = "hasRole('ADMIN') OR hasRole('MANAGER') OR hasRole('STUDENT')";


    @PreAuthorize(ADMIN_OR_MANAGER)
    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseDto request) {

        Course createdCourse = courseService.persist(courseMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                courseMapper.toDTO(createdCourse)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id,
                                                  @Valid @RequestBody CourseDto request) {

        request.setId(id);
        Course persist = courseService.persist(courseMapper.toEntity(request));

        return ResponseEntity.status(HttpStatus.OK).body(
                courseMapper.toDTO(persist)
        );

    }

    @PreAuthorize(ADMIN_OR_MANAGER)
    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.status(HttpStatus.OK).body(
                courseService.findAll().stream()
                        .map(courseMapper::toDTO)
                        .toList()
        );
    }

    @PreAuthorize(ALL_AUTHENTICATED)
    @GetMapping("/major/{majorId}")
    public ResponseEntity<List<CourseDto>> getCourseByMajor(@PathVariable Long majorId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                courseService.findAllMajorCourse(majorId).stream()
                        .map(courseMapper::toDTO)
                        .toList()
        );
    }


    @DeleteMapping("/{courseId}")
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteById(courseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
