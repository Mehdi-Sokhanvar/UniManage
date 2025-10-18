package org.unimanage.service.imple;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.unimanage.config.CustomUserDetails;
import org.unimanage.config.CustomUserDetailsService;
import org.unimanage.domain.course.Major;
import org.unimanage.domain.user.Account;
import org.unimanage.domain.user.Person;
import org.unimanage.domain.user.Role;
import org.unimanage.repository.AccountRepository;
import org.unimanage.repository.MajorRepository;
import org.unimanage.repository.PersonRepository;
import org.unimanage.repository.RoleRepository;
import org.unimanage.service.AuthService;
import org.unimanage.util.enumration.AccountStatus;
import org.unimanage.util.exception.EntityNotFoundException;
import org.unimanage.util.message.ErrorMessage;
import org.unimanage.util.dto.AccountRequestDto;
import org.unimanage.util.dto.AuthResponseDto;
import org.unimanage.util.exception.AccessDeniedException;
import org.unimanage.util.exception.EntityExistsException;
import org.unimanage.util.jwt.JwtUtil;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthServiceImpl extends BaseServiceImpl<Person, Long> implements AuthService {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final MajorRepository majorRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final MessageSource messageSource;

    public AuthServiceImpl(PersonRepository personRepository, RoleRepository roleRepository,
                           MajorRepository majorRepository, AccountRepository accountRepository,
                           PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService, MessageSource messageSource) {
        super(personRepository, messageSource);
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.majorRepository = majorRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.messageSource = messageSource;
    }


//    @Override
//    public void registerPerson(Person person, String role) {
//        Role roleExist = roleRepository.findByName(role)
//                .orElseThrow(() ->
//                        new org.unimanage.util.exception
//                                .EntityNotFoundException(messageSource.getMessage("error.role.not.found", new Object[]{role}, LocaleContextHolder.getLocale())));
//        person.getAccount().setRoles(Set.of(roleExist));
//        this.persist(person);
//    }

    @Override
    public void activeAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new org.unimanage.util.exception.EntityNotFoundException(messageSource.getMessage("account.not.found", new Object[]{accountId}, LocaleContextHolder.getLocale())));

        account.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);
    }

    @Override
    public void logOut(String token) {
        Jws<Claims> claimsJws = jwtUtil.validateToken(token);

        String authToken = claimsJws.getBody().get("auth-id").toString();

        Account account = accountRepository.findByAuthId(UUID.fromString(authToken))
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("account.not.found", new Object[]{authToken}, LocaleContextHolder.getLocale())));

        account.setAuthId(null);
        accountRepository.save(account);
    }


    @Override
    protected void prePersist(Person entity) {
        if (personRepository.existsByNationalCode(entity.getNationalCode())) {
            String errorMessage = messageSource.getMessage("error.person.already.exists", new Object[]{entity.getNationalCode()}, LocaleContextHolder.getLocale());
            throw new EntityExistsException(errorMessage);
        }
        Major major = majorRepository.findByName(entity.getMajor().getName())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.major.not.found", new Object[]{entity.getMajor().getName()}, LocaleContextHolder.getLocale())));
        entity.setMajor(major);
    }

    @Override
    protected void postPersist(Person entity) {
        entity.setAccount(Account.builder()
                .username(entity.getNationalCode())
                .password(passwordEncoder.encode(entity.getPhoneNumber()))
                .status(AccountStatus.INACTIVE)
                .build());
        personRepository.save(entity);
    }


    @Override
    public AuthResponseDto login(AccountRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Account account = userDetails.getAccount();
        if (!account.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new AccessDeniedException(ErrorMessage.ACCOUNT_NOT_ACTIVE.format(account.getUsername()));
        }
        account.setAuthId(UUID.randomUUID());
        accountRepository.save(account);
        String accessToken = jwtUtil.generateAccessToken(account.getAuthId());
        String refreshToken = jwtUtil.generateRefreshToken(account.getAuthId());

        return new AuthResponseDto(accessToken, refreshToken, "Bearer");

    }

    @Override
    public void addRoleToAccount(String role, Long accountId) {
        Locale locale = LocaleContextHolder.getLocale();
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("account.not.found", new Object[]{accountId}, LocaleContextHolder.getLocale())));

        boolean hasRole = account.getRoles().stream()
                .anyMatch(r -> r.getName().equals(role));
        if (hasRole) {
            messageSource.getMessage(
                    "error.person.already.has.role",
                    new Object[]{account.getUsername(), role},
                    locale
            );
        }
        Role roleExist = roleRepository.findByName(role)
                .orElseThrow(() -> new org.unimanage.util.exception.EntityNotFoundException(messageSource.getMessage("error.role.not.found", new Object[]{role}, locale)));
        account.getRoles().add(roleExist);
        accountRepository.save(account);
    }

    @Override
    public Set<Role> getPersonRoles(Principal principal) {
        String username = principal.getName();
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USERNAME_NOT_FOUND.format(username)));
        return account.getRoles();
    }

    @Override
    public void rePassword(String oldPassword, String newPassword, Principal principal) {
        String username = principal.getName();
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage("account.not.found", new Object[]{username}, LocaleContextHolder.getLocale())));
        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new AccessDeniedException(messageSource.getMessage("error.password.not.match", null, LocaleContextHolder.getLocale()));
        }
        account.setAuthId(null);
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    @Override
    public void forgotPassword(String emailOrPhone) {
        //todo: how to manage that in this section
    }

    @Override
    public AuthResponseDto refreshToken(String refreshToken) {
        Jws<Claims> claimsJws = jwtUtil.validateToken(refreshToken);
        Account account = accountRepository.findByAuthId((UUID) claimsJws.getBody().get("auth-id"))
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND.format("with given token")));
        if (!account.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new AccessDeniedException(ErrorMessage.ACCOUNT_NOT_ACTIVE.format(account.getUsername()));
        }
        String newAccessToken = jwtUtil.generateAccessToken(account.getAuthId());
        String newRefreshToken = jwtUtil.generateRefreshToken(account.getAuthId());
        return new AuthResponseDto(newAccessToken, newRefreshToken, "Bearer");
    }


    @Override
    public void deleteById(Long aLong) {
        Account account = accountRepository.findById(aLong)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("account.not.found", new Object[]{aLong}, LocaleContextHolder.getLocale())));
        account.setStatus(AccountStatus.INACTIVE);
        accountRepository.save(account);
    }


}


