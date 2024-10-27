package com.energy.tajo.points.domain;

import com.energy.tajo.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Charge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int pointsSpent;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Column(nullable = false, name="total_points_after_transaction")
    private int totalPointsAfterTransaction;

    @Column(nullable = false, name="charge_account")
    private String chargeAccount;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "uuid")
    private User user;

    private Charge(User user, int pointsSpent, int totalPointsAfterTransaction, String chargeAccount) {
        this.user = user;
        this.pointsSpent = pointsSpent;
        this.totalPointsAfterTransaction = totalPointsAfterTransaction;
        this.chargeAccount = chargeAccount;
        this.transactionDate = LocalDateTime.now();
    }

    public static Charge of(User user, int pointsSpent, int totalPointsAfterTransaction, String chargeAccount) {
        return new Charge(user, pointsSpent, totalPointsAfterTransaction, chargeAccount);
    }
}
