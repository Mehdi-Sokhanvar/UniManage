package org.unimanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unimanage.domain.course.Major;

import java.util.List;
import java.util.Optional;


@Repository
public interface MajorRepository extends JpaRepository<Major,Long> {
    Optional<Major> findByMajorName(String majorName);
}
