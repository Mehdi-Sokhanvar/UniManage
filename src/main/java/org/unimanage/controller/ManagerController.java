package org.unimanage.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.unimanage.domain.course.Course;
import org.unimanage.service.CourseService;
import org.unimanage.util.dto.ApiResponse;
import org.unimanage.util.dto.CourseDto;
import org.unimanage.util.dto.PersonRegisterDto;
import org.unimanage.util.dto.mapper.CourseMapper;
import org.unimanage.util.dto.mapper.PersonMapper;

import java.time.Instant;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class ManagerController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;
    private final PersonMapper personMapper;


    @GetMapping("/course-major/{majorName}")
    @PreAuthorize("hasRole('MANAGER') OR @securityService.isManagerOfMajor(#majorName)")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getCourseMajor(@PathVariable String majorName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<CourseDto>>builder()
                                .success(true)
                                .data(courseService.findAllCourseByMajorName(majorName).stream()
                                        .map(courseMapper::toDTO)
                                        .toList())
                                .timestamp(Instant.now().toString())
                                .build()
                );
    }

    @GetMapping("/teacher/{majorName}")
    @PreAuthorize("hasRole('MANAGER') OR @securityService.isManagerOfMajor(#majorName)")
    public ResponseEntity<ApiResponse<List<PersonRegisterDto>>> getTeacherMajor(@PathVariable String majorName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<PersonRegisterDto>>builder()
                                .success(true)
                                .message("Teacher Major")
                                .data(courseService.findAllTeacherByMajorName(majorName).stream()
                                        .map(personMapper::toDTO)
                                        .toList())
                                .timestamp(Instant.now().toString())
                                .build()
                );
    }



}
