package org.unimanage.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.unimanage.UniManageApplication;
import org.unimanage.controller.util.TestDataFactory;
import org.unimanage.domain.course.Course;
import org.unimanage.domain.course.Major;
import org.unimanage.domain.user.Account;
import org.unimanage.repository.*;
import org.unimanage.util.dto.AccountRequestDto;
import org.unimanage.util.dto.AuthResponseDto;
import org.unimanage.util.dto.CourseDto;
import org.unimanage.util.enumration.AccountStatus;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = UniManageApplication.class
)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private MajorRepository majorRepository;


    private Account adminAccount;
    private Account manageAccount;
    private Major major;
    private String ADMIN_TOKEN = null;
    private String MANAGER_TOKEN = null;

    private final TestDataFactory testDataFactory = new TestDataFactory();


    @BeforeEach
    public void setup() throws Exception {
        courseRepository.deleteAll();
        majorRepository.deleteAll();
        personRepository.deleteAll();
        setupRolesAndAccounts();
    }

    void setupRolesAndAccounts() throws Exception {
        adminAccount = Account.builder()
                .username("admin_username")
                .password(passwordEncoder.encode("password"))
                .roles(new HashSet<>(List.of(roleRepository.findByName("ADMIN").get())))
                .status(AccountStatus.ACTIVE)
                .build();

        manageAccount = Account.builder()
                .username("manage_username")
                .password(passwordEncoder.encode("password"))
                .roles(new HashSet<>(List.of(roleRepository.findByName("MANAGER").get())))
                .status(AccountStatus.ACTIVE)
                .build();


        accountRepository.save(adminAccount);
        accountRepository.save(manageAccount);

        AccountRequestDto adminDto = AccountRequestDto.builder()
                .username(adminAccount.getUsername())
                .password("password")
                .build();
        AccountRequestDto managerDto = AccountRequestDto.builder()
                .username(manageAccount.getUsername())
                .password("password")
                .build();

        ResultActions resultActionsAdmin = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(adminDto)))
                .andExpect(status().isOk());
        String loginAdminResponseBody = resultActionsAdmin.andReturn().getResponse().getContentAsString();
        AuthResponseDto adminResponseLogin = objectMapper.readValue(loginAdminResponseBody, AuthResponseDto.class);
        ADMIN_TOKEN = adminResponseLogin.getAccessToken();

        ResultActions resultActionsManager = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(managerDto)))
                .andExpect(status().isOk());
        String loginManagerResponseBody = resultActionsManager.andReturn().getResponse().getContentAsString();
        AuthResponseDto managerResponseLogin = objectMapper.readValue(loginManagerResponseBody, AuthResponseDto.class);
        MANAGER_TOKEN = managerResponseLogin.getAccessToken();

        major = majorRepository.save(Major.builder().name("Computer Science").active(true).numberOfUnits(12).build());
    }

    @Test
    void Should_Admin_Or_Manager_Create_Course() throws Exception {
        CourseDto dto = CourseDto.builder()
                .name("Computer Science 101")
                .majorName(major.getName())
                .build();

        CourseDto dto1 = CourseDto.builder()
                .name("Computer Science 102")
                .majorName(major.getName())
                .build();

        mockMvc.perform(
                post("/api/courses")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isCreated());

        mockMvc.perform(
                post("/api/courses")
                        .header("Authorization", "Bearer " + MANAGER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1))
        ).andExpect(status().isCreated());
    }

    @Test
    void Should_Return_Forbidden_When_User_Not_Admin_Or_Manager_Create_Course() throws Exception {
        CourseDto dto = CourseDto.builder()
                .name("Computer Science 103")
                .majorName(major.getName())
                .build();

        mockMvc.perform(
                post("/api/courses")
                        .header("Authorization", "Bearer someInvalidToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void Should_Admin_Or_Manager_Get_All_Courses() throws Exception {
        mockMvc.perform(
                get("/api/courses")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
        ).andExpect(status().isOk());

        mockMvc.perform(
                get("/api/courses")
                        .header("Authorization", "Bearer " + MANAGER_TOKEN)
        ).andExpect(status().isOk());
    }

    @Test
    void Should_Admin_Or_Manager_Get_All_Course_By_Major() throws Exception {
        String majorName = "Computer Science";

        Major majorSaved = majorRepository.save(
                Major.builder()
                        .name(majorName)
                        .courses(courseRepository.saveAll(
                                List.of(
                                        Course.builder().name("Algorithm").active(true).build(),
                                        Course.builder().name("Network").active(true).build(),
                                        Course.builder().name("Database").active(true).build(),
                                        Course.builder().name("Architecture").active(true).build()
                                )))
                        .build()
        );


        mockMvc.perform(
                get("/api/courses/major/" + majorSaved.getId())
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
        ).andExpect(status().isOk());

        mockMvc.perform(
                get("/api/courses/major/" + majorSaved.getId())
                        .header("Authorization", "Bearer " + MANAGER_TOKEN)
        ).andExpect(status().isOk());
    }


    @Test
    void Should_Admin_Or_Manger_Delete_Course() throws Exception {
        String majorName = "Computer Science";

        Major majorSaved = majorRepository.save(
                Major.builder()
                        .name(majorName)
                        .courses(courseRepository.saveAll(
                                List.of(
                                        Course.builder().name("Algorithm").active(true).build(),
                                        Course.builder().name("Network").active(true).build(),
                                        Course.builder().name("Database").active(true).build(),
                                        Course.builder().name("Architecture").active(true).build()
                                )))
                        .build()
        );

        mockMvc.perform(
                delete("/api/courses/" + majorSaved.getCourses().get(0).getId())
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
        ).andExpect(status().isNoContent());
        mockMvc.perform(
                delete("/api/courses/" + majorSaved.getCourses().get(0).getId())
                        .header("Authorization", "Bearer " + MANAGER_TOKEN  )
        ).andExpect(status().isNoContent());
    }


    @Test
    void Should_Return_Bad_Request_In_Valid_Data() throws Exception {
        String invalidCourseJson = "{\"name\": \"\", \"majorName\": \"\"}"; // Invalid data

        mockMvc.perform(
                post("/api/courses")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCourseJson)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void Should_Return_Conflict_of_Major_Courses() throws Exception {
        String majorName = "example major";
        String duplicateCourseName = "Algorithms";
        Major majorSaved = majorRepository.save(
                Major.builder()
                        .name(majorName)
                        .build()
        );
        courseRepository.saveAll(
                List.of(
                        Course.builder().name(duplicateCourseName).active(true).major(majorSaved).build()
                ));

        CourseDto requestDto= CourseDto.builder()
                .name(duplicateCourseName)
                .majorName(majorName)
                .build();
        mockMvc.perform(
                post("/api/courses")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        ).andExpect(status().isConflict());
    }


    private Long extractCourseIdFromResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(responseBody).get("id").asLong();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract course ID", e);
        }
    }

}