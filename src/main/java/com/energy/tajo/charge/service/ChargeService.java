package com.energy.tajo.charge.service;

import com.energy.tajo.global.exception.EnergyException;
import com.energy.tajo.global.exception.ErrorCode;
import com.energy.tajo.charge.dto.response.ChargeResponse;
import com.energy.tajo.charge.repository.ChargeRepository;
import com.energy.tajo.user.domain.User;
import com.energy.tajo.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChargeService {

    private final ChargeRepository chargeRepository;
    private final UserRepository userRepository;

    // 충전 내역 조회
    public List<ChargeResponse> getPointsTransactions(String userId) {
        User user = userRepository.findByUuid(userId)
            .orElseThrow(() -> new EnergyException(ErrorCode.USER_NOT_FOUND));
        return chargeRepository.findByUserOrderByTransactionDateDesc(user).stream()
            .map(ChargeResponse::from)
            .toList();
    }
}
