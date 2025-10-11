package org.unimanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.unimanage.domain.course.Term;
import org.unimanage.domain.course.TermType;
import org.unimanage.util.enumration.TermStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {
    List<Term> findTermByTermStatus(TermStatus termStatus);

    List<Term> findTermByYear(int year);

    boolean existsByTermTypeAndMajorIdAndYear(TermType termType, Long majorId, Long year);

    @Query("SELECT t FROM Term t WHERE (t.termStatus = 'ACTIVE') AND " +
            "t.major.name =: majorName AND " +
            "t.year =:   year AND " +
            "t.termType =: type")
    List<Term> findTermByMajor_Name(
            @Param("majorName") String majorName,
            @Param("year") int year,
            @Param("type") TermType termType);
}
