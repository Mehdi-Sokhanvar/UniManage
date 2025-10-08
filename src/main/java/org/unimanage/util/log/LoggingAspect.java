package org.unimanage.util.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.unimanage.domain.log.Logs;
import org.unimanage.repository.LogRepository;

import java.time.LocalDateTime;

@Aspect
@RequiredArgsConstructor
public class LoggingAspect {

    private final LogRepository repository;
    private final HttpServletRequest request;
    private final Authentication authentication;

    public Object logAction(ProceedingJoinPoint joinPoint) throws Throwable {
    }
}
