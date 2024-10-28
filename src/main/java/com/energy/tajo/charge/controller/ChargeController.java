package com.energy.tajo.charge.controller;

import com.energy.tajo.charge.dto.response.ChargeResponse;
import com.energy.tajo.charge.service.ChargeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/charge")
@RequiredArgsConstructor
public class ChargeController {

    private final ChargeService chargeService;

    // 로그인된 사용자 자신의 충전 내역 조회
    @GetMapping
    public ResponseEntity<List<ChargeResponse>> getMyChargeTransactions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserUuid = (String) authentication.getPrincipal();

        List<ChargeResponse> chargeTransactions = chargeService.getPointsTransactions(authenticatedUserUuid);
        return ResponseEntity.ok(chargeTransactions);
    }
}