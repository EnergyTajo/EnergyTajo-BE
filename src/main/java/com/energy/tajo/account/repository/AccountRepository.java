package com.energy.tajo.account.repository;

import com.energy.tajo.account.domain.Account;
import com.energy.tajo.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByAccountNum(String accountNum);
    Optional<Account> findByAccountNumAndBankName(String accountNum, String bankName);
    List<Account> findByUser(User user);
}