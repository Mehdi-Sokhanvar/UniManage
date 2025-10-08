//package org.unimanage.controller;
//
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.unimanage.domain.course.Term;
//import org.unimanage.service.CourseOfferingService;
//import org.unimanage.service.TermService;
//import org.unimanage.util.dto.ApiResponse;
//import org.unimanage.util.message.ApiResponseMessage;
//
//import org.unimanage.util.dto.TermDto;
//import org.unimanage.util.dto.mapper.TermMapper;
//
//@RestController
//@RequestMapping("/api/terms/")
//@RequiredArgsConstructor
//@Tag(name = "Term Management", description = "Endpoints for managing Term")
//public class TermController {
//
//    private final TermService termService;
//    private final CourseOfferingService courseOfferingService;
//    private final TermMapper termMapper;
//
//
//    private static final String ADMIN_OR_MANAGER = "hasRole('ADMIN') OR hasRole('MANAGER')";
//    private static final String ALL_AUTHENTICATED = "hasRole('ADMIN') OR hasRole('MANAGER') OR hasRole('STUDENT')";
//
//
//    @PreAuthorize(ADMIN_OR_MANAGER)
//    @PostMapping
//    public ResponseEntity<ApiResponse<Object>> createTerm(@RequestBody TermDto termDto) {
//        Term term = termService.persist(termMapper.toEntity(termDto));
//        ApiResponse<Object> response =
//                ApiResponse.builder()
//                        .message(ApiResponseMessage.TERM_SUCCESSFULLY_ADD_TO_TERM.format(term.getMajor().getName()))
//                        .data(termMapper.toDTO(term))
//                        .build();
//        return ResponseEntity.ok(response);
//    }
//
//
//    @PreAuthorize(ADMIN_OR_MANAGER)
//    @PutMapping
//    public ResponseEntity<ApiResponse<Object>> updateTerm(@RequestBody TermDto termDto) {
//        Term term = termService.persist(termMapper.toEntity(termDto));
//        ApiResponse<Object> response =
//                ApiResponse.builder()
//                        .message(ApiResponseMessage.TERM_SUCCESSFULLY_ADD_TO_TERM.format(term.getMajor().getName()))
//                        .data(termMapper.toDTO(term))
//                        .build();
//        return ResponseEntity.ok(response);
//    }
//
//
//
//
//}
//
