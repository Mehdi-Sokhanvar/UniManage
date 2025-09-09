package org.unimanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unimanage.domain.user.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long> {
}
