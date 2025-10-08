package org.unimanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.unimanage.domain.log.Logs;

@Repository
public interface LogRepository extends JpaRepository<Logs, Long> {

}
