package com.energy.tajo.card.repository;

import com.energy.tajo.card.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByCardNum(String cardNum);
}
