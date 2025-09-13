package org.unimanage.service;

import org.unimanage.domain.user.Person;

public interface AuthService extends BaseService<Person,Long> {
    Person registerStudent(Person teacher);
    Person addTeacher(Person teacher);
}
