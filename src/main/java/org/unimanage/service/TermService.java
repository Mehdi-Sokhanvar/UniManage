package org.unimanage.service;

import org.unimanage.domain.course.OfferedCourse;
import org.unimanage.domain.course.Term;
import org.unimanage.domain.user.Person;
import org.unimanage.util.enumration.TermStatus;

import java.security.Principal;
import java.util.List;

public interface TermService extends BaseService<Term,Long>{
        List<Term> getAllTerms(String username);
}
