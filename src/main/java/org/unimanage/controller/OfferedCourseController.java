package org.unimanage.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unimanage.domain.course.OfferedCourse;
import org.unimanage.service.CourseOfferingService;
import org.unimanage.util.dto.ApiResponse;
import org.unimanage.util.dto.OfferedCourseDTO;
import org.unimanage.util.dto.mapper.OfferedCourseMapper;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/offered-courses")
public class OfferedCourseController {

    private final CourseOfferingService courseOfferingService;
    private final OfferedCourseMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<OfferedCourseDTO>> createOfferedCourse(
            @Valid
            @RequestBody OfferedCourseDTO offeredCourse) {
        OfferedCourse persist = courseOfferingService.persist(mapper.toEntity(offeredCourse));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<OfferedCourseDTO>builder()
                        .message("created successfully")
                        .success(true)
                        .data(mapper.toDTO(persist))
                        .timestamp(Instant.now().toString())
                        .build());
    }

    @PutMapping("{offercourseId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<OfferedCourseDTO>> updateOfferedCourse(
            @PathVariable Long offercourseId,
            @Valid @RequestBody OfferedCourseDTO offeredCourse) {
        OfferedCourse entity = mapper.toEntity(offeredCourse);
        entity.setId(offercourseId);
        OfferedCourse persist = courseOfferingService.persist(entity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<OfferedCourseDTO>builder()
                        .message("created successfully")
                        .success(true)
                        .data(mapper.toDTO(persist))
                        .timestamp(Instant.now().toString())
                        .build());
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<OfferedCourseDTO>> deleteOfferedCourse(@PathVariable Long id) {
        courseOfferingService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    // student choose course
    // student get all course term
    // student can print

//    @GetMapping("/{termId}")
//    @PreAuthorize("hasRole('STUDENT') OR @securityService.")
//    public ResponseEntity<ApiResponse<OfferedCourseDTO>> getOfferedCourse(@PathVariable Long termId) {
//
//
//    }



}
