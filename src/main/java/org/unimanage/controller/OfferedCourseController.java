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
import org.unimanage.util.dto.StudentCourse;
import org.unimanage.util.dto.StudentScheduleDto;
import org.unimanage.util.dto.mapper.OfferedCourseMapper;
import org.unimanage.util.dto.mapper.StudentScheduleMapper;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/offered-courses")
public class OfferedCourseController {

    private final CourseOfferingService courseOfferingService;
    private final OfferedCourseMapper mapper;
    private final StudentScheduleMapper scheduleMapper;


    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<OfferedCourseDTO> createOfferedCourse(@Valid @RequestBody OfferedCourseDTO offeredCourse) {
        OfferedCourse persist = courseOfferingService.persist(mapper.toEntity(offeredCourse));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toDTO(persist));
    }

    @PutMapping("{offerCourseId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<OfferedCourseDTO> updateOfferedCourse(@PathVariable Long offerCourseId, @Valid @RequestBody OfferedCourseDTO offeredCourse) {
        OfferedCourse entity = mapper.toEntity(offeredCourse);
        entity.setId(offerCourseId);
        OfferedCourse persist = courseOfferingService.persist(entity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toDTO(persist));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteOfferedCourse(@PathVariable Long id) {
        courseOfferingService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/{termId}")
    @PreAuthorize("hasRole('STUDENT') AND @securityService.isStudentOfMajor(#termId)")
    public ResponseEntity<List<StudentScheduleDto>> getOfferedCourse(@PathVariable Long termId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        courseOfferingService.findOfferedCoursesByTermId(termId).stream()
                                .map(scheduleMapper::toDTO)
                                .toList()
                );
    }


    @PostMapping("/{termId}/take-course/{courseId}")
    @PreAuthorize("hasRole('STUDENT') AND @securityService.isStudentOfMajor(#termId)")
    public ResponseEntity<String> studentGetCourse(@PathVariable Long courseId, Principal principal, @PathVariable String termId) {
        courseOfferingService.getStudentCourse(courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body("success get Course");
    }


    @GetMapping("/get-student/{offeredCourseId}")
    @PreAuthorize("hasRole('TEACHER') AND @securityService.isTeacherOfOfferedCourse(#offeredCourseId)")
    public ResponseEntity<List<StudentCourse>> teacherGetAllCourseWithStudent(@PathVariable Long offeredCourseId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                courseOfferingService.getAllStudentsByOfferedCourseId(offeredCourseId)
        );
    }


}
