package com.energy.tajo.user.repository;

import com.energy.tajo.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUuid(String uuid);
    boolean existsByPhoneNum(String phoneNum);
    Optional<User> findByUuid(String uuid);
}