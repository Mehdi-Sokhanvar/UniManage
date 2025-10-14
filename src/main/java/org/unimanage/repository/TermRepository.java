package org.unimanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.unimanage.domain.course.Semester;
import org.unimanage.domain.course.Term;
import org.unimanage.util.enumration.TermStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

    List<Term> findTermByYear(int year);

    boolean existsBySemesterAndMajorIdAndYear(Semester Semester, Long majorId, int year);


    List<Term> findTermByMajor_Name(String majorName);
}