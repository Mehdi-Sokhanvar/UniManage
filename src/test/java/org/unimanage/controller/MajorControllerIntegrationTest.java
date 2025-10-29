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
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.repository.AccountRepository;
import org.unimanage.repository.PersonRepository;
import org.unimanage.repository.RoleRepository;
import org.unimanage.util.dto.AccountRequestDto;
import org.unimanage.util.dto.AuthResponseDto;
import org.unimanage.util.dto.MajorDto;
import org.unimanage.util.enumration.AccountStatus;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = UniManageApplication.class
)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class MajorControllerIntegrationTest {


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

    private Account adminAccount;

    private String ADMIN_TOKEN = null;

    private final TestDataFactory testDataFactory = new TestDataFactory();


    @BeforeEach
    public void setup() throws Exception {
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
        accountRepository.save(adminAccount);
        AccountRequestDto adminDto = AccountRequestDto.builder()
                .username(adminAccount.getUsername())
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
    }


    @Test
    void Should_Admin_Create_Major_Successfully() throws Exception {
        MajorDto majorDto = MajorDto.builder()
                .name("Computer Science")
                .numberOfUnits((byte) 21)
                .build();

        mockMvc.perform(post("/api/majors")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(majorDto)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    MajorDto response = objectMapper.readValue(result.getResponse().getContentAsString(), MajorDto.class);
                    assertEquals("Computer Science", response.getName());
                    assertNotNull(response.getId());
                });
    }

    @Test
    void Should_Admin_Update_Major_Successfully() throws Exception {
        MajorDto majorDto = MajorDto.builder().name("Mathematics").numberOfUnits((byte) 21).build();
        String createResponse = mockMvc.perform(post("/api/majors")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(majorDto)))
                .andReturn().getResponse().getContentAsString();
        MajorDto createdMajor = objectMapper.readValue(createResponse, MajorDto.class);
        createdMajor.setName("Applied Mathematics");
        mockMvc.perform(put("/api/majors/" + createdMajor.getId())
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdMajor)))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    MajorDto updatedMajor = objectMapper.readValue(result.getResponse().getContentAsString(), MajorDto.class);
                    assertEquals("Applied Mathematics", updatedMajor.getName());
                });
    }

    @Test
    void Should_Admin_Get_Major_Successfully() throws Exception {
        MajorDto majorDto = MajorDto.builder().name("Physics").numberOfUnits((byte) 21).build();
        String createResponse = mockMvc.perform(post("/api/majors")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(majorDto)))
                .andReturn().getResponse().getContentAsString();
        MajorDto createdMajor = objectMapper.readValue(createResponse, MajorDto.class);

        mockMvc.perform(get("/api/majors/" + createdMajor.getId())
                        .header("Authorization", "Bearer " + ADMIN_TOKEN))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MajorDto fetched = objectMapper.readValue(result.getResponse().getContentAsString(), MajorDto.class);
                    assertEquals("Physics", fetched.getName());
                });
    }

    @Test
    void Should_Admin_Delete_Major_Successfully() throws Exception {
        MajorDto majorDto = MajorDto.builder().name("Chemistry").numberOfUnits((byte) 21).build();
        String createResponse = mockMvc.perform(post("/api/majors")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(majorDto)))
                .andReturn().getResponse().getContentAsString();
        MajorDto createdMajor = objectMapper.readValue(createResponse, MajorDto.class);

        mockMvc.perform(delete("/api/majors/" + createdMajor.getId())
                        .header("Authorization", "Bearer " + ADMIN_TOKEN))
                .andExpect(status().isNoContent());

        // Verify it’s inactive
        mockMvc.perform(get("/api/majors/" + createdMajor.getId())
                        .header("Authorization", "Bearer " + ADMIN_TOKEN))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MajorDto fetched = objectMapper.readValue(result.getResponse().getContentAsString(), MajorDto.class);
                });
    }

    @Test
    void Should_Admin_Get_All_Major_Successfully() throws Exception {
        MajorDto major1 = MajorDto.builder().name("Biology").numberOfUnits((byte) 21).build();
        MajorDto major2 = MajorDto.builder().name("History").numberOfUnits((byte) 21).build();

        mockMvc.perform(post("/api/majors")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(major1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/majors")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(major2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/majors")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MajorDto[] majors = objectMapper.readValue(result.getResponse().getContentAsString(), MajorDto[].class);
                    assertTrue(majors.length >= 2);
                });
    }

    @Test
    void Should_Bad_Request_For_Invalid_Data() throws Exception {
        MajorDto invalidMajor = MajorDto.builder().name("").build();

        mockMvc.perform(post("/api/majors")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMajor)))
                .andExpect(status().isBadRequest());
    }

}