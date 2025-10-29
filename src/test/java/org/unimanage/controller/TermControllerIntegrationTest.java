package org.unimanage.controller;

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
import org.unimanage.domain.course.Major;
import org.unimanage.domain.course.Semester;
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.repository.*;
import org.unimanage.util.dto.AccountRequestDto;
import org.unimanage.util.dto.AuthResponseDto;
import org.unimanage.util.dto.TermDto;
import org.unimanage.util.enumration.AccountStatus;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = UniManageApplication.class
)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class TermControllerIntegrationTest {

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
    private Account studentAccount;
    private Major major;
    private String ADMIN_TOKEN = null;
    private String MANAGER_TOKEN = null;
    private String STUDENT_TOKEN = null;

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
                .person(Person.builder().roles(new HashSet<>(List.of(roleRepository.findByName("ADMIN").get()))).build())

                .status(AccountStatus.ACTIVE)
                .build();

        manageAccount = Account.builder()
                .username("manage_username")
                .password(passwordEncoder.encode("password"))
                .person(Person.builder().roles(new HashSet<>(List.of(roleRepository.findByName("MANAGER").get()))).build())
                .status(AccountStatus.ACTIVE)
                .build();
        studentAccount = Account.builder()
                .username("student_username")
                .password(passwordEncoder.encode("password"))
                .person(Person.builder().roles(new HashSet<>(List.of(roleRepository.findByName("STUDENT").get()))).build())
                .status(AccountStatus.ACTIVE)
                .build();


        accountRepository.save(adminAccount);
        accountRepository.save(manageAccount);
        accountRepository.save(studentAccount);

        AccountRequestDto adminDto = AccountRequestDto.builder()
                .username(adminAccount.getUsername())
                .password("password")
                .build();
        AccountRequestDto managerDto = AccountRequestDto.builder()
                .username(manageAccount.getUsername())
                .password("password")
                .build();
        AccountRequestDto studentDto = AccountRequestDto.builder()
                .username(studentAccount.getUsername())
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


        ResultActions resultActionsStudent = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isOk());
        String loginStudentResponseBody = resultActionsStudent.andReturn().getResponse().getContentAsString();
        AuthResponseDto studentResponseLogin = objectMapper.readValue(loginStudentResponseBody, AuthResponseDto.class);
        STUDENT_TOKEN = studentResponseLogin.getAccessToken();

        major = majorRepository.save(Major.builder().name("Computer Science").active(true).numberOfUnits(12).build());
    }

    @Test
    void Should_Admin_CreateTerm_Successfully() throws Exception {

        TermDto term = TermDto.builder()
                .semester(Semester.FALL)
                .majorName(major.getName())
                .courseRegistrationStart(LocalDate.now().plusDays(1))
                .courseRegistrationEnd(LocalDate.now().plusDays(10))
                .addDropStart(LocalDate.now().plusDays(11))
                .addDropEnd(LocalDate.now().plusDays(15))
                .classesStart(LocalDate.now().plusDays(16))
                .classesEnd(LocalDate.now().plusDays(120))
                .finalExam(LocalDate.now().plusDays(125))
                .build();

        mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(term))
        ).andExpect(status().isCreated());
    }


    @Test
    void Should_Manager_CreateTerm_Successfully() throws Exception {

        TermDto term = TermDto.builder()
                .semester(Semester.FALL)
                .majorName(major.getName())
                .courseRegistrationStart(LocalDate.now().plusDays(1))
                .courseRegistrationEnd(LocalDate.now().plusDays(10))
                .addDropStart(LocalDate.now().plusDays(11))
                .addDropEnd(LocalDate.now().plusDays(15))
                .classesStart(LocalDate.now().plusDays(16))
                .classesEnd(LocalDate.now().plusDays(120))
                .finalExam(LocalDate.now().plusDays(125))
                .build();

        mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + MANAGER_TOKEN)
                .content(objectMapper.writeValueAsString(term))
        ).andExpect(status().isCreated());

    }

    @Test
    void Should_Student_CreateTerm_AccessDenied() throws Exception {
        TermDto term = TermDto.builder()
                .semester(Semester.FALL)
                .majorName(major.getName())
                .courseRegistrationStart(LocalDate.now().plusDays(1))
                .courseRegistrationEnd(LocalDate.now().plusDays(10))
                .addDropStart(LocalDate.now().plusDays(11))
                .addDropEnd(LocalDate.now().plusDays(15))
                .classesStart(LocalDate.now().plusDays(16))
                .classesEnd(LocalDate.now().plusDays(120))
                .finalExam(LocalDate.now().plusDays(125))
                .build();

        mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + STUDENT_TOKEN)
                .content(objectMapper.writeValueAsString(term))
        ).andExpect(status().isForbidden());

    }

    @Test
    void Should_Admin_CreateTerm_InvalidData_Fail() throws Exception {

        TermDto term = TermDto.builder()
                .semester(Semester.FALL)
                .majorName(major.getName())
                .courseRegistrationStart(LocalDate.now().plusDays(1))
                .courseRegistrationEnd(LocalDate.now().minusDays(10))
                .addDropStart(LocalDate.now().plusDays(11))
                .addDropEnd(LocalDate.now().minusDays(15))
                .classesStart(LocalDate.now().plusDays(16))
                .classesEnd(LocalDate.now().minusDays(120))
                .finalExam(LocalDate.now().plusDays(125))
                .build();

        mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(term))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void Should_Admin_CreateTerm_TermAlreadyExists_Fail() throws Exception {
        TermDto term = TermDto.builder()
                .semester(Semester.FALL)
                .majorName(major.getName())
                .courseRegistrationStart(LocalDate.now().plusDays(1))
                .courseRegistrationEnd(LocalDate.now().plusDays(10))
                .addDropStart(LocalDate.now().plusDays(11))
                .addDropEnd(LocalDate.now().plusDays(15))
                .classesStart(LocalDate.now().plusDays(16))
                .classesEnd(LocalDate.now().plusDays(120))
                .finalExam(LocalDate.now().plusDays(125))
                .build();

        mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(term))
        ).andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(term))
        ).andExpect(status().isConflict());
    }

    @Test
    void Should_Admin_UpdateTerm_Successfully() throws Exception {
        TermDto term = TermDto.builder()
                .semester(Semester.FALL)
                .majorName(major.getName())
                .courseRegistrationStart(LocalDate.now().plusDays(1))
                .courseRegistrationEnd(LocalDate.now().plusDays(10))
                .addDropStart(LocalDate.now().plusDays(11))
                .addDropEnd(LocalDate.now().plusDays(15))
                .classesStart(LocalDate.now().plusDays(16))
                .classesEnd(LocalDate.now().plusDays(120))
                .finalExam(LocalDate.now().plusDays(125))
                .build();


        ResultActions resultActions = mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(term))
        ).andExpect(status().isCreated());

        TermDto responseDto = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), TermDto.class);
        responseDto.setSemester(Semester.SUMMER);
        responseDto.setMajorName(major.getName());

        mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(responseDto))
        ).andExpect(status().isConflict());

    }

    @Test
    void Should_Manager_UpdateTerm_Successfully() throws Exception {
        TermDto term = TermDto.builder()
                .semester(Semester.FALL)
                .majorName(major.getName())
                .courseRegistrationStart(LocalDate.now().plusDays(1))
                .courseRegistrationEnd(LocalDate.now().plusDays(10))
                .addDropStart(LocalDate.now().plusDays(11))
                .addDropEnd(LocalDate.now().plusDays(15))
                .classesStart(LocalDate.now().plusDays(16))
                .classesEnd(LocalDate.now().plusDays(120))
                .finalExam(LocalDate.now().plusDays(125))
                .build();


        ResultActions resultActions = mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(term))
        ).andExpect(status().isCreated());

        TermDto responseDto = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), TermDto.class);
        responseDto.setSemester(Semester.SUMMER);
        responseDto.setMajorName(major.getName());

        mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + MANAGER_TOKEN)
                .content(objectMapper.writeValueAsString(responseDto))
        ).andExpect(status().isConflict());
    }

    @Test
    void Should_Student_UpdateTerm_AccessDenied() throws Exception {
        TermDto term = TermDto.builder()
                .semester(Semester.FALL)
                .majorName(major.getName())
                .courseRegistrationStart(LocalDate.now().plusDays(1))
                .courseRegistrationEnd(LocalDate.now().plusDays(10))
                .addDropStart(LocalDate.now().plusDays(11))
                .addDropEnd(LocalDate.now().plusDays(15))
                .classesStart(LocalDate.now().plusDays(16))
                .classesEnd(LocalDate.now().plusDays(120))
                .finalExam(LocalDate.now().plusDays(125))
                .build();


        ResultActions resultActions = mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(term))
        ).andExpect(status().isCreated());

        TermDto responseDto = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), TermDto.class);
        responseDto.setSemester(Semester.SUMMER);
        responseDto.setMajorName(major.getName());

        mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + STUDENT_TOKEN)
                .content(objectMapper.writeValueAsString(responseDto))
        ).andExpect(status().isConflict());
    }

    @Test
    void Should_Admin_UpdateTerm_TermNotFound_Fail() throws Exception {
        Random random = new Random();

        TermDto term = TermDto.builder()
                .semester(Semester.FALL)
                .majorName(major.getName())
                .courseRegistrationStart(LocalDate.now().plusDays(1))
                .courseRegistrationEnd(LocalDate.now().plusDays(10))
                .addDropStart(LocalDate.now().plusDays(11))
                .addDropEnd(LocalDate.now().plusDays(15))
                .classesStart(LocalDate.now().plusDays(16))
                .classesEnd(LocalDate.now().plusDays(120))
                .finalExam(LocalDate.now().plusDays(125))
                .build();

        mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(term))
        ).andExpect(status().isNotFound());
    }

    @Test
    void Should_Admin_UpdateTerm_InvalidData_Fail() throws Exception {
        TermDto term = TermDto.builder()
                .semester(Semester.FALL)
                .majorName(major.getName())
                .courseRegistrationStart(LocalDate.now().plusDays(1))
                .courseRegistrationEnd(LocalDate.now().minusDays(10))
                .addDropStart(LocalDate.now().plusDays(11))
                .addDropEnd(LocalDate.now().minusDays(15))
                .classesStart(LocalDate.now().plusDays(16))
                .classesEnd(LocalDate.now().minusDays(120))
                .finalExam(LocalDate.now().plusDays(125))
                .build();

        mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(term))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void Should_Admin_DeleteTerm_Successfully() throws Exception {
        TermDto term = TermDto.builder()
                .semester(Semester.FALL)
                .majorName(major.getName())
                .courseRegistrationStart(LocalDate.now().plusDays(1))
                .courseRegistrationEnd(LocalDate.now().plusDays(10))
                .addDropStart(LocalDate.now().plusDays(11))
                .addDropEnd(LocalDate.now().plusDays(15))
                .classesStart(LocalDate.now().plusDays(16))
                .classesEnd(LocalDate.now().plusDays(120))
                .finalExam(LocalDate.now().plusDays(125))
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(term))
        ).andExpect(status().isBadRequest());

        TermDto responseDto = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), TermDto.class);

        mockMvc.perform(delete("/api/term" + responseDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
        ).andExpect(status().isNoContent());
    }

    @Test
    void Should_Manager_DeleteTerm_Successfully() throws Exception {
        TermDto term = TermDto.builder()
                .semester(Semester.FALL)
                .majorName(major.getName())
                .courseRegistrationStart(LocalDate.now().plusDays(1))
                .courseRegistrationEnd(LocalDate.now().plusDays(10))
                .addDropStart(LocalDate.now().plusDays(11))
                .addDropEnd(LocalDate.now().plusDays(15))
                .classesStart(LocalDate.now().plusDays(16))
                .classesEnd(LocalDate.now().plusDays(120))
                .finalExam(LocalDate.now().plusDays(125))
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(term))
        ).andExpect(status().isBadRequest());

        TermDto responseDto = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), TermDto.class);

        mockMvc.perform(delete("/api/term" + responseDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
        ).andExpect(status().isNoContent());
    }

    @Test
    void Should_Student_DeleteTerm_AccessDenied() throws Exception {

        TermDto term = TermDto.builder()
                .semester(Semester.FALL)
                .majorName(major.getName())
                .courseRegistrationStart(LocalDate.now().plusDays(1))
                .courseRegistrationEnd(LocalDate.now().plusDays(10))
                .addDropStart(LocalDate.now().plusDays(11))
                .addDropEnd(LocalDate.now().plusDays(15))
                .classesStart(LocalDate.now().plusDays(16))
                .classesEnd(LocalDate.now().plusDays(120))
                .finalExam(LocalDate.now().plusDays(125))
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/term")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(term))
        ).andExpect(status().isBadRequest());

        TermDto responseDto = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), TermDto.class);

        mockMvc.perform(delete("/api/term" + responseDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + STUDENT_TOKEN)
        ).andExpect(status().isForbidden());
    }

    @Test
    void Should_Admin_DeleteTerm_TermNotFound_Fail() throws Exception {
        Random random = new Random();
        int randomNumber = random.nextInt(10000);
        mockMvc.perform(delete("/api/term" + randomNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
        ).andExpect(status().isForbidden());
    }



    @Test
    void Should_Student_GetAllTerms_AccessDenied() throws Exception {
        mockMvc.perform(get("/api/term")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + STUDENT_TOKEN))
                .andExpect(status().isForbidden());
    }


    @Test
    void Should_Admin_GetStudentTerms_Successfully() throws Exception {
        TermDto term = TermDto.builder()
                .semester(Semester.FALL)
                .majorName(major.getName())
                .courseRegistrationStart(LocalDate.now().plusDays(1))
                .courseRegistrationEnd(LocalDate.now().plusDays(10))
                .addDropStart(LocalDate.now().plusDays(11))
                .addDropEnd(LocalDate.now().plusDays(15))
                .classesStart(LocalDate.now().plusDays(16))
                .classesEnd(LocalDate.now().plusDays(120))
                .finalExam(LocalDate.now().plusDays(125))
                .build();

        mockMvc.perform(post("/api/term")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .content(objectMapper.writeValueAsString(term)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/term")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ADMIN_TOKEN))
                .andExpect(status().isOk());
    }
}