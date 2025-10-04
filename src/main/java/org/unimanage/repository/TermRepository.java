package org.unimanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unimanage.domain.course.Term;
import org.unimanage.util.enumration.TermStatus;

import java.util.List;

@Repository
public interface TermRepository extends JpaRepository<Term,Long> {
    List<Term> findTermByTermStatus(TermStatus termStatus);

    List<Term> findTermByYear(int year);
}
