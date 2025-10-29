package org.unimanage.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.domain.user.Role;
import org.unimanage.repository.AccountRepository;
import org.unimanage.repository.PersonRepository;
import org.unimanage.repository.RoleRepository;
import org.unimanage.service.PersonService;
import org.unimanage.util.dto.AccountRequestDto;
import org.unimanage.util.dto.AuthResponseDto;
import org.unimanage.util.dto.RePasswordDto;
import org.unimanage.util.enumration.AccountStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = UniManageApplication.class
)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class PersonControllerIntegrationTest {

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

    private String ADMIN_TOKEN = null;
    private String STUDENT_TOKEN = null;
    private String TEACHER_TOKEN = null;

    private Account studentAccount;
    private Account teacherAccount;
    private Account adminAccount;


    private TestDataFactory testDataFactory = new TestDataFactory();

    @BeforeEach
    public void setUp() throws Exception {
        personRepository.deleteAll();
        accountRepository.deleteAll();
        setupRolesAndAccounts();

        AccountRequestDto adminDto = AccountRequestDto.builder()
                .username(adminAccount.getUsername())
                .password("password")
                .build();
        AccountRequestDto studentDto = AccountRequestDto.builder()
                .username(studentAccount.getUsername())
                .password("password")
                .build();
        AccountRequestDto teacherDto = AccountRequestDto.builder()
                .username(teacherAccount.getUsername())
                .password("password")
                .build();

        ResultActions resultActionsAdmin = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(adminDto)))
                .andExpect(status().isOk());

        ResultActions resultActionsTeacher = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(teacherDto)))
                .andExpect(status().isOk());
        ResultActions resultActionsStudent = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isOk());

        String loginAdminResponseBody = resultActionsAdmin.andReturn().getResponse().getContentAsString();
        String loginTeacherResponseBody = resultActionsTeacher.andReturn().getResponse().getContentAsString();
        String loginStudentResponseBody = resultActionsStudent.andReturn().getResponse().getContentAsString();

        AuthResponseDto adminResponseLogin = objectMapper.readValue(loginAdminResponseBody, AuthResponseDto.class);
        AuthResponseDto teacherResponseLogin = objectMapper.readValue(loginTeacherResponseBody, AuthResponseDto.class);
        AuthResponseDto studentResponseLogin = objectMapper.readValue(loginStudentResponseBody, AuthResponseDto.class);


        ADMIN_TOKEN = adminResponseLogin.getAccessToken();
        TEACHER_TOKEN = teacherResponseLogin.getAccessToken();
        STUDENT_TOKEN = studentResponseLogin.getAccessToken();
    }

    void setupRolesAndAccounts() throws Exception {


        studentAccount = Account.builder()
                .username("student_username")
                .password(passwordEncoder.encode("password"))
                .person(Person.builder().roles(new HashSet<>(List.of(roleRepository.findByName("STUDENT").get()))).build())

                .status(AccountStatus.ACTIVE)
                .build();
        adminAccount = Account.builder()
                .username("admin_username")
                .password(passwordEncoder.encode("password"))
                .person(Person.builder().roles(new HashSet<>(List.of(roleRepository.findByName("ADMIN").get()))).build())

                .status(AccountStatus.ACTIVE)
                .build();
        teacherAccount = Account.builder()
                .username("teacher_username")
                .password(passwordEncoder.encode("password"))
                .person(Person.builder().roles(new HashSet<>(List.of(roleRepository.findByName("TEACHER").get()))).build())

                .status(AccountStatus.ACTIVE)
                .build();

//        accountRepository.saveAll(List.of(studentAccount, teacherAccount, adminAccount));
        accountRepository.save(studentAccount);
        accountRepository.save(adminAccount);
        accountRepository.save(teacherAccount);
    }


    @Test
    void should_Successfully_Change_Password_For_Admin() throws Exception {
        RePasswordDto requestDto = RePasswordDto.builder()
                .oldPassword("password")
                .newPassword("newPassword")
                .build();

        mockMvc.perform(put("/api/user/password")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(requestDto))

        ).andExpect(status().isOk());

    }

    @Test
    void should_Successfully_Change_Password_For_Student() throws Exception {
        RePasswordDto requestDto = RePasswordDto.builder()
                .oldPassword("password")
                .newPassword("newPassword")
                .build();

        mockMvc.perform(put("/api/user/password")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + STUDENT_TOKEN)
                .content(objectMapper.writeValueAsString(requestDto))

        ).andExpect(status().isOk());

    }

    @Test
    void should_Successfully_Change_Password_For_Teacher() throws Exception {
        RePasswordDto requestDto = RePasswordDto.builder()
                .oldPassword("password")
                .newPassword("newPassword")
                .build();

        mockMvc.perform(put("/api/user/password")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TEACHER_TOKEN)
                .content(objectMapper.writeValueAsString(requestDto))

        ).andExpect(status().isOk());

    }

    @Test
    void should_Return_Unauthorized_When_No_Authorization_Header() throws Exception {
        RePasswordDto requestDto = RePasswordDto.builder()
                .oldPassword("password")
                .newPassword("newPassword")
                .build();
        mockMvc.perform(put("/api/user/password")
                .contentType(MediaType.APPLICATION_JSON)  // request doesn't have token
                .content(objectMapper.writeValueAsString(requestDto))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void should_Return_Unauthorized_When_Malformed_Authorization_Header() throws Exception {
        RePasswordDto requestDto = RePasswordDto.builder()
                .oldPassword("password")
                .newPassword("newPassword")
                .build();
        mockMvc.perform(put("/api/user/password")
                .contentType(MediaType.APPLICATION_JSON)  // request doesn't have token
                .header("Authorization", ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(requestDto))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void should_Return_Forbidden_When_OldPassword_Is_Incorrect() throws Exception {
        RePasswordDto requestDto = RePasswordDto.builder()
                .oldPassword("wrong old password")
                .newPassword("newPassword")
                .build();

        mockMvc.perform(put("/api/user/password")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(requestDto))

        ).andExpect(status().isForbidden());

    }


    @Test
    void should_Return_BadRequest_When_OldPassword_Is_Blank() throws Exception {
        RePasswordDto dto = RePasswordDto.builder()
                .oldPassword(" ")
                .newPassword(" ")
                .build();
        mockMvc.perform(put("/api/user/password")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void should_Prevent_Sql_Injection_In_OldPassword() throws Exception {

        RePasswordDto request = RePasswordDto.builder()
                .oldPassword("' OR '1'='1'; DROP TABLE accounts; --")
                .newPassword("NewPassword123!")
                .build();
        mockMvc.perform(put("/api/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

    }


    @Test
    void should_Return_All_Student_Roles() throws Exception {
        mockMvc.perform(get("/api/user/roles")
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + STUDENT_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roleName").value("STUDENT"));
    }
    @Test
    void should_Return_All_Admin_Roles() throws Exception {
        mockMvc.perform(get("/api/user/roles")
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + ADMIN_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roleName").value("ADMIN"));
    }
    @Test
    void should_Return_All_Teacher_Roles() throws Exception {
        mockMvc.perform(get("/api/user/roles")
                        .content(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + TEACHER_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roleName").value("TEACHER"));
    }
}