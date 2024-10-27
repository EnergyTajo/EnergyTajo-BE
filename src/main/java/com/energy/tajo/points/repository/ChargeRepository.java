package com.energy.tajo.points.repository;

import com.energy.tajo.points.domain.Charge;
import com.energy.tajo.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeRepository extends JpaRepository<Charge, Long> {
    List<Charge> findByUser(User user);
}
