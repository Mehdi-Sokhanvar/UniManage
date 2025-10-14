package org.unimanage.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.unimanage.domain.user.Person;

@Service
public class SecurityService {

    public boolean isManagerOfMajor(String majorName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person) authentication.getPrincipal();
        return person.getMajor().getName().equals(majorName);
    }

    public boolean isStudentOfMajor(String majorName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = (Person) authentication.getPrincipal();
        return person.getMajor().getName().equals(majorName);
    }
}
