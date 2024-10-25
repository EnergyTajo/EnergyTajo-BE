package com.energy.tajo.card.domain;

import com.energy.tajo.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="card_id")
    private Long cardId;

    @ManyToOne
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid", nullable = false)
    private User user;

    @Column(nullable = false, name="card_num")
    private String cardNum;

    @Column(nullable = false, name="valid_thru")
    private String validThru;

    @Column(nullable = false)
    private String cvc;

    @Column(updatable = false, name="created_at")
    private java.sql.Timestamp createdAt = new java.sql.Timestamp(System.currentTimeMillis());
}
