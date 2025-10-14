package org.unimanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unimanage.domain.user.Person;
import org.unimanage.domain.user.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long> {
    Optional<Person> findByNationalCode(String nationalCode);
    boolean existsByNationalCode(String nationalCode);
    List<Person> findAllByMajor_Name(String majorName);
//    boolean existsPersonByIdContains(Long personId, String roleName);

//    List<Role> findRolesB(Long personId);
}
