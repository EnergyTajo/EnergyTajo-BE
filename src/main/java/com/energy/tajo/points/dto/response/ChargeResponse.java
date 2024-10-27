package com.energy.tajo.points.dto.response;

import com.energy.tajo.points.domain.Charge;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChargeResponse {
    private Long id;
    private int pointsSpent;
    private LocalDate transactionDate;
    private int totalPointsAfterTransaction;
    private String accountNumLastFourDigits;

    public static ChargeResponse from(Charge charge) {
        return new ChargeResponse(
            charge.getId(),
            charge.getPointsSpent(),
            charge.getTransactionDate().toLocalDate(),
            charge.getTotalPointsAfterTransaction(),
            charge.getChargeAccount()
        );
    }
}
