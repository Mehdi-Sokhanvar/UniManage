package org.unimange;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.repository.AccountRepository;
import org.unimanage.repository.PersonRepository;
import org.unimanage.util.dto.AccountRequestDto;
import org.unimanage.util.dto.AuthResponseDto;
import org.unimanage.util.dto.PersonRegisterDto;
import org.unimanage.util.enumration.AccountStatus;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = UniManageApplication.class
)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthControllerTest {

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

    private final TestDataFactory testDataFactory = new TestDataFactory();


    @BeforeEach
    public void setup() {
        personRepository.deleteAll();
    }

    @Test
    public void should_register_student_successfully() throws Exception {
        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDataFactory.studentSuccessfullyData()))
        ).andExpect(status().isCreated());
    }

    @Test
    public void should_return_conflict_when_duplicate_national_code() throws Exception {
        Person existing = personRepository.save(Person.builder()
                .nationalCode("1234567899")
                .build());

        PersonRegisterDto dto = PersonRegisterDto.builder()
                .firstName("Student")
                .lastName("Test")
                .email("example@gmail.com")
                .nationalCode(existing.getNationalCode())
                .phoneNumber("09000000000")
                .majorName("علوم کامپیوتر")
                .build();

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    public void should_return_not_found_when_major_not_exist() throws Exception {

        PersonRegisterDto build = PersonRegisterDto.builder()
                .firstName("student_name")
                .lastName("student_family_name")
                .email("example@gmail.com")
                .nationalCode("1234567890")
                .phoneNumber("09000000000")
                .majorName("Unknown major")
                .build();

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(build))
        ).andExpect(status().isNotFound());
    }


    @Test
    public void should_login_active_user_successfully() throws Exception {
        Person person = createPersonWithAccount("123456790", "09000000000", AccountStatus.ACTIVE);
        AccountRequestDto dto = AccountRequestDto.builder()
                .username(person.getNationalCode())
                .password(person.getPhoneNumber())
                .build();

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk());

    }

    @Test
    public void should_forbid_login_for_inactive_user() throws Exception {
        Person person = createPersonWithAccount("123456791", "09000000001", AccountStatus.INACTIVE);
        AccountRequestDto dto = AccountRequestDto.builder()
                .username(person.getNationalCode())
                .password(person.getPhoneNumber())
                .build();
        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isForbidden());

    }

    @Test
    public void should_unauthorized_for_invalid_credentials() throws Exception {
        AccountRequestDto dto = AccountRequestDto.builder()
                .username("wronguser")
                .password("wrongpass")
                .build();
        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isUnauthorized());

    }

    @Test
    public void should_return_bad_request_for_invalid_register_data() throws Exception {
        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDataFactory.studentNotValidData()))
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_bad_request_for_invalid_login_data() throws Exception {
        AccountRequestDto accountRequestDto = AccountRequestDto.builder()
                .username(testDataFactory.studentNotValidData().getNationalCode())
                .password(testDataFactory.studentNotValidData().getPhoneNumber())
                .build();
        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDataFactory.studentNotValidData()))
        ).andExpect(status().isBadRequest());
    }


    @Test
    public void should_logout_successfully() throws Exception {

        Person person = createPersonWithAccount("123456792", "09000000002", AccountStatus.ACTIVE);
        AccountRequestDto dto = AccountRequestDto.builder()
                .username(person.getNationalCode())
                .password(person.getPhoneNumber())
                .build();

        ResultActions resultActions = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        String loginResponseBody = resultActions.andReturn().getResponse().getContentAsString();
        AuthResponseDto login = objectMapper.readValue(loginResponseBody, AuthResponseDto.class);
        String accessToken = login.getAccessToken();

        mockMvc.perform(
                post("/auth/logout")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }


    private Person createPersonWithAccount(String nationalCode, String phoneNumber, AccountStatus status) {
        Person person = Person.builder()
                .firstName("Student")
                .lastName("Test")
                .email("student@example.com")
                .nationalCode(nationalCode)
                .phoneNumber(phoneNumber)
                .build();

        Account account = accountRepository.save(Account.builder()
                .username(nationalCode)
                .password(passwordEncoder.encode(phoneNumber))
                .status(status)
                .authId(null)
                .build());

        Person save = personRepository.save(person);
        save.setAccount(account);
        personRepository.save(person);
        return save;
    }
}