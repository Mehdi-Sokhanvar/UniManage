package org.unimanage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    private static final String ADMIN_OR_MANAGER= "hasRole('ADMIN') OR hasRole('MANAGER')";
    private static final String ALL_AUTHENTICATED = "hasRole('ADMIN') OR hasRole('MANAGER') OR hasRole('STUDENT')";


    @PreAuthorize(ADMIN_OR_MANAGER)
    @PostMapping
    public ResponseEntity<ApiResponse<CourseDto>> createCourse(@RequestBody CourseDto request) {

        Course createdCourse = courseService.persist(courseMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<CourseDto>builder()
                        .success(true)
                        .message(messageSource.getMessage("course.creation.success", null, LocaleContextHolder.getLocale()))
                        .data(courseMapper.toDTO(createdCourse))
                        .timestamp(Instant.now().toString())
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResponse<CourseDto>> updateCourse(@PathVariable Long id,
                                                               @RequestBody CourseDto request) {

        request.setId(id);
        Course persist = courseService.persist(courseMapper.toEntity(request));

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<CourseDto>builder()
                        .success(true)
                        .message(messageSource.getMessage("course.update.success", null, LocaleContextHolder.getLocale()))
                        .data(courseMapper.toDTO(persist))
                        .timestamp(Instant.now().toString())
                        .build()
        );

    }

    @PreAuthorize(ADMIN_OR_MANAGER)
    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseDto>>> getAllCourses() {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<CourseDto>>builder()
                        .success(true)
                        .message(messageSource.getMessage("courses.get.success", null, LocaleContextHolder.getLocale()))
                        .data(courseService.findAll().stream()
                                .map(courseMapper::toDTO)
                                .toList())
                        .timestamp(Instant.now().toString())
                        .build()
        );
    }

    @PreAuthorize(ALL_AUTHENTICATED)
    @GetMapping("major/{majorId}")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getCourseByMajor(@PathVariable Long majorId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<CourseDto>>builder()
                        .success(true)
                        .message(messageSource.getMessage("courses.get.success", null, LocaleContextHolder.getLocale()))
                        .data(courseService.findAllMajorCourse(majorId).stream()
                                .map(courseMapper::toDTO)
                                .toList())
                        .timestamp(Instant.now().toString())
                        .build()
        );
    }


    @DeleteMapping("/{courseId}")
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteById(courseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
