package com.energy.tajo.charge.repository;

import com.energy.tajo.charge.domain.Charge;
import com.energy.tajo.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeRepository extends JpaRepository<Charge, Long> {
    List<Charge> findByUserOrderByTransactionDateDesc(User user);
}
