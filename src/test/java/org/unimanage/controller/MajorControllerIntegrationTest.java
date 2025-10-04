package org.unimanage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.unimanage.domain.course.Course;
import org.unimanage.domain.course.Major;
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Role;
import org.unimanage.repository.*;
import org.unimanage.util.dto.MajorDto;
import org.unimanage.util.jwt.JwtUtil;

import java.util.Random;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MajorControllerIntegrationTest{
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    protected Major testMajor;
    protected Course testCourse;
    protected Role testUSERRole;
    protected Role testAdminRole;
    protected Account testNormalAccount;
    protected Account testAdminAccount;
    protected String validNormalJwtToken;
    protected String validAdminJwtToken;


    @BeforeEach
    void setUp() {
        for (Account account : accountRepository.findAll()) {
            account.setActiveRole(null);
            account.setPerson(null);
            accountRepository.save(account);
        }

        accountRepository.deleteAll();
        personRepository.deleteAll();
        roleRepository.deleteAll();
        courseRepository.deleteAll();
        majorRepository.deleteAll();

        setupTestData();
        setupAuthenticationToken();
    }


    private void setupTestData() {
        testMajor = majorRepository.save(Major.builder()
                .majorName("Computer Science")
                .numberOfUnits((byte) 120)
                .majorCode(UUID.randomUUID())
                .active(true)
                .build());


        testCourse = courseRepository.save(Course.builder()
                .major(testMajor)
                .courseName("Data structures")
                .active(true)
                .build());

        testAdminRole = roleRepository.save(Role.builder()
                .roleName("ADMIN")
                .build());

        testUSERRole = roleRepository.save(Role.builder()
                .roleName("STUDENT")
                .build());


        testNormalAccount = accountRepository.save(Account.builder()
                .username("normal")
                .password(passwordEncoder.encode("password"))
                .authId(UUID.randomUUID())
                .activeRole(testUSERRole)
                .build());

        testAdminAccount = accountRepository.save(Account.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .authId(UUID.randomUUID())
                .activeRole(testAdminRole)
                .build());

    }

    private void setupAuthenticationToken() {
        validNormalJwtToken = jwtUtil.generateAccessToken(testNormalAccount.getAuthId());
        validAdminJwtToken = jwtUtil.generateAccessToken(testAdminAccount.getAuthId());
    }
    @Test
    void should_return_401_when_No_Authentication_provided() throws Exception {
        mockMvc.perform(
                        get("/api/majors"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void should_return_403_when_No_Authentication_provided() throws Exception {
        mockMvc.perform(
                        get("/api/majors")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + validNormalJwtToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void should_return_200_Created_Major_Successfully() throws Exception {

        MajorDto majorDto = MajorDto.builder()
                .majorName("Electronics")
                .numberOfUnits((byte) 120)
                .build();

        mockMvc.perform(
                post("/api/majors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(majorDto))
                        .header("Authorization", "Bearer " + validAdminJwtToken)
        ).andExpect(status().isOk());
    }

    @Test
    void should_return_200_Updated_Major_Successfully() throws Exception {
        MajorDto majorDto = MajorDto.builder()
                .majorName("Update major")
                .numberOfUnits((byte) 120)
                .build();

        mockMvc.perform(
                post("/api/majors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(majorDto))
                        .header("Authorization", "Bearer " + validAdminJwtToken)
        ).andExpect(status().isOk());
    }


    @Test
    void should_Return_409_When_Creating_Major_AlreadyExists() throws Exception {

        mockMvc.perform(
                post("/api/majors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMajor))
                        .header("Authorization", "Bearer " + validAdminJwtToken)
        ).andExpect(status().isConflict());


    }

    @Test
    void should_Return_404_When_Does_Not_Exist() throws Exception {
        Random random = new Random();
        mockMvc.perform(
                delete("/api/majors/" + random.nextInt(100,155))
                        .header("Authorization", "Bearer " + validAdminJwtToken)
        ).andExpect(status().isNotFound());
    }

    @Test
    void should_Return_No_Content_When_Soft_Delete_Major_Successfully() throws Exception {
        mockMvc.perform(
                delete("/api/majors/" + testMajor.getId())
                        .header("Authorization", "Bearer " + validAdminJwtToken)
        ).andExpect(status().isNoContent());
    }
}