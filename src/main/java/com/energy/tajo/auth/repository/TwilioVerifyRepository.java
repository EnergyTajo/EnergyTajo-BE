package com.energy.tajo.auth.repository;

import com.energy.tajo.auth.domain.TwilioVerify;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwilioVerifyRepository extends JpaRepository<TwilioVerify, Long> {
}