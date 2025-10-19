package org.unimanage.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unimanage.domain.course.OfferedCourse;
import org.unimanage.domain.user.Role;
import org.unimanage.service.AuthService;
import org.unimanage.service.CourseOfferingService;
import org.unimanage.util.dto.*;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Person Management", description = "Endpoints for managing academic courses")

public class PersonController {

    private final AuthService authService;
    private final MessageSource messageSource;
    private final CourseOfferingService courseOfferingService;

    @PutMapping("/password")
    @PreAuthorize("hasRole('STUDENT') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<String> changePassword(Principal principal, @RequestBody RePasswordDto request, Locale locale) {
        authService.rePassword(request.getOldPassword(), request.getNewPassword(), principal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(messageSource.getMessage("success.change.password", new Object[]{principal.getName()}, locale));
    }


    @GetMapping("/roles")
    @PreAuthorize("hasRole('STUDENT') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<RoleResponse>> getPersonRoles(Principal principal, Locale locale) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.getPersonRoles(principal).stream()
                        .map(role -> new RoleResponse(role.getName()))
                        .toList());
    }

    @GetMapping("/get-course/{termId}")
    @PreAuthorize("hasRole('STUDENT') AND @securityService.isStudentOfMajor(#termId)")
    public ResponseEntity<List<CourseRegistrationDTO>> studentGetCourse(@PathVariable Long termId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                courseOfferingService.findAllCourseByTermId(termId)
        );
    }

    @GetMapping("/get-schedule/{termId}")
    @PreAuthorize("hasRole('TEACHER') AND @securityService.isTeacherOfMajor(#termId)")
    public ResponseEntity<List<OfferedCourseTeacherDto>> teacherGetAllCourse(@PathVariable Long termId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                courseOfferingService.getTeacherScheduleTime(termId).stream()
                        .map(course -> OfferedCourseTeacherDto.builder()
                                .courseName(course.getCourse().getName())
                                .startTime(course.getStartTime())
                                .endTime(course.getEndTime())
                                .dayOfWeek(course.getDayOfWeek())
                                .build())
                        .toList());
    }



}

