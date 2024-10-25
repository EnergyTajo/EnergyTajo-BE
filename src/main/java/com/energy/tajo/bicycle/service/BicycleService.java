package com.energy.tajo.bicycle.service;

import com.energy.tajo.bicycle.domain.Bicycle;
import com.energy.tajo.bicycle.dto.response.BicycleResponse;
import com.energy.tajo.bicycle.repository.BicycleRepository;
import com.energy.tajo.global.exception.EnergyException;
import com.energy.tajo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BicycleService {

    private final BicycleRepository bicycleRepository;

    public Bicycle findBicycleById(String bicycleId) {
        return bicycleRepository.findByBicycleId(bicycleId);
    }

    // QR 코드로 자전거 정보 조회 및 검증 로직
    public BicycleResponse getBicycleInfo(String bicycleId) {
        validateBicycleId(bicycleId);
        Bicycle bicycle = findBicycleById(bicycleId);

        if (bicycle == null) {
            throw new EnergyException(ErrorCode.BICYCLE_NOT_FOUND);
        }
        if (bicycle.isActive()) {
            throw new EnergyException(ErrorCode.BICYCLE_ALREADY_IN_USE);
        }

        return new BicycleResponse(bicycle);
    }

    // 배터리 상태 업데이트
    public void updateBatteryStat(String bicycleId, int generatedPower) {
        Bicycle bicycle = findBicycleById(bicycleId);

        if (bicycle != null) {
            int updatedPowerGeneratedTotal = bicycle.getPowerGeneratedTotal() + generatedPower;
            int updatedBatteryStat = (int) ((double) updatedPowerGeneratedTotal / 3000 * 100);

            bicycle.setPowerGeneratedTotal(updatedPowerGeneratedTotal);
            bicycle.setBatteryStat(updatedBatteryStat);
            bicycleRepository.save(bicycle);
        }
    }

    // QR 코드 유효성 검증
    private void validateBicycleId(String bicycleId) {
        String regex = "^ENERGYTAJO051(JG|YJG|DG|NG)[A-C]\\d{3}$";
        if (!bicycleId.matches(regex)) {
            throw new EnergyException(ErrorCode.INVALID_QR_CODE);
        }
    }
}
