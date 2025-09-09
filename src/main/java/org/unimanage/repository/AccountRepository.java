package org.unimanage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unimanage.domain.user.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Boolean existsAccountByUsername(String username);
}
