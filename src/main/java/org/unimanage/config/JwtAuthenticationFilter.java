package org.unimanage.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.unimanage.domain.user.Account;
import org.unimanage.repository.AccountRepository;
import org.unimanage.util.exception.EntityNotFoundException;
import org.unimanage.util.jwt.JwtUtil;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AccountRepository accountRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, AccountRepository accountRepository, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.accountRepository = accountRepository;
        this.customUserDetailsService = customUserDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = extractToken(request);
            if (token != null) {
                authenticateToken(token);
            }
        } catch (EntityNotFoundException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Account not found for token");
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }

        filterChain.doFilter(request, response);

    }

    private String extractToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

    private void authenticateToken(String token) {
        Jws<Claims> claimsJws = jwtUtil.validateToken(token);
        UUID authId = UUID.fromString((String) claimsJws.getBody().get("auth-id"));

        Account account = accountRepository.findByAuthId(authId)
                .orElseThrow(() -> new EntityNotFoundException("Invalid token"));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(account.getUsername());

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
