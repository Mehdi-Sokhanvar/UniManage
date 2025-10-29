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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.unimanage.UniManageApplication;
import org.unimanage.controller.util.TestDataFactory;
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.repository.AccountRepository;
import org.unimanage.repository.PersonRepository;
import org.unimanage.repository.RoleRepository;
import org.unimanage.util.dto.AccountRequestDto;
import org.unimanage.util.dto.AddRoleRequest;
import org.unimanage.util.dto.AuthResponseDto;
import org.unimanage.util.dto.PersonRegisterDto;
import org.unimanage.util.enumration.AccountStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = UniManageApplication.class
)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class AdminControllerIntegrationTest {


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
    private Account manageAccount;

    private String ADMIN_TOKEN = null;
    private String MANAGER_TOKEN = null;

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
        manageAccount = Account.builder()
                .username("manage_username")
                .password(passwordEncoder.encode("password"))
                .person(Person.builder().roles(new HashSet<>(List.of(roleRepository.findByName("MANAGER").get()))).build())
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

    }

    @Test
    public void Should_Admin_Register_Teacher_OR_MANAGER_Successfully() throws Exception {
        mockMvc.perform(
                post("/api/admin/register/teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .content(objectMapper.writeValueAsString(testDataFactory.teacherSuccessfullyData()))
        ).andExpect(status().isCreated());
        mockMvc.perform(
                post("/api/admin/register/manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .content(objectMapper.writeValueAsString(testDataFactory.managerSuccessfullyData()))
        ).andExpect(status().isCreated());
    }

    @Test
    public void Should_Manager_Register_Teacher_Successfully() throws Exception {
        mockMvc.perform(
                post("/api/admin/register/teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDataFactory.teacherSuccessfullyData()))
                        .header("Authorization", "Bearer " + MANAGER_TOKEN)
        ).andExpect(status().isCreated());
    }


    @Test
    public void Should_Admin_Add_Roles_To_Person_Successfully() throws Exception {
        AddRoleRequest requestDto = AddRoleRequest.builder()
                .roleName("TEACHER")
                .build();
        mockMvc.perform(
                        post("/api/admin/users/" + manageAccount.getId() + "/roles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void Should_Admin_Activate_Account_Successfully() throws Exception {
        Account sampleAccount = accountRepository.save(Account.builder()
                .username("manage_username")
                .password(passwordEncoder.encode("password"))
                .person(Person.builder().roles(new HashSet<>(List.of(roleRepository.findByName("MANAGER").get()))).build())
                .status(AccountStatus.INACTIVE)
                .build());
        mockMvc.perform(
                        put("/api/admin/active/account/" + sampleAccount.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void Should_Not_Found_Account_When_Account_Does_Not_Exist() throws Exception {
        Random random = new Random();
        int accountId = random.nextInt(10) + 1;
        mockMvc.perform(
                        put("/api/admin/active/account/" + accountId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void Should_Conflict_When_Account_Has_Role() throws Exception {
        AddRoleRequest requestDto = AddRoleRequest.builder()
                .roleName("MANAGER")
                .build();
        mockMvc.perform(
                        post("/api/admin/users/" + manageAccount.getId() + "/roles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict());
    }

    @Test
    public void Should_Not_Found_Role_When_Role_Does_Not_Exist() throws Exception {
        AddRoleRequest requestDto = AddRoleRequest.builder()
                .roleName("wrong role")
                .build();
        mockMvc.perform(
                        post("/api/admin/users/" + manageAccount.getId() + "/roles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void Should_Bad_Request_For_Invalid_Data() throws Exception {
        AddRoleRequest invalidRoleRequest = AddRoleRequest.builder()
                .roleName("INVALID_ROLE") // غیر مجاز
                .description("") // اختیاری، مشکلی نیست
                .build();

        mockMvc.perform(post("/api/admin/users/1/roles")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRoleRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void Should_Bad_Request_For_Register_Invalid_Data() throws Exception {
        PersonRegisterDto invalidDto = PersonRegisterDto.builder()
                .firstName("") // کوتاه‌تر از 1
                .lastName(null) // null
                .nationalCode("123") // کمتر از 10 رقم
                .phoneNumber("abcd") // فرمت نادرست
                .email("invalid-email") // فرمت نادرست
                .majorName("") // کوتاه‌تر از 1
                .build();
        mockMvc.perform(post("/api/admin/register/manager")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }


}