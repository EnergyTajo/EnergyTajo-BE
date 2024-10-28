package com.energy.tajo.ride.service;

import com.energy.tajo.bicycle.domain.Bicycle;
import com.energy.tajo.bicycle.repository.BicycleRepository;
import com.energy.tajo.bicycle.service.BicycleService;
import com.energy.tajo.global.exception.EnergyException;
import com.energy.tajo.global.exception.ErrorCode;
import com.energy.tajo.ride.domain.Ride;
import com.energy.tajo.ride.dto.response.RideEndResponse;
import com.energy.tajo.ride.dto.response.RideUsageHistory;
import com.energy.tajo.ride.repository.RideRepository;
import com.energy.tajo.user.domain.User;
import com.energy.tajo.user.repository.UserRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepository;
    private final BicycleService bicycleService;
    private final UserRepository userRepository;
    private final BicycleRepository bicycleRepository;

    // 이용 시작
    public Ride startRide(String bicycleId, String userUuid) {
        Bicycle bicycle = bicycleService.findBicycleById(bicycleId);
        if (bicycle == null) {
            throw new EnergyException(ErrorCode.BICYCLE_NOT_FOUND);
        }
        if (bicycle.isActive()) {
            throw new EnergyException(ErrorCode.BICYCLE_ALREADY_IN_USE);
        }

        bicycle.setActive(true);
        bicycleRepository.save(bicycle);

        Ride ride = new Ride();
        ride.setUserId(userUuid);
        ride.setBicycle(bicycle);
        ride.setStartTime(LocalDateTime.now());
        ride.setStartRideDate(LocalDate.now());
        return rideRepository.save(ride);
    }

    // 이용 종료
    public RideEndResponse endRide(Long rideId, String userUuid) {
        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new EnergyException(ErrorCode.RIDE_NOT_FOUND));

        if (!ride.getUserId().equals(userUuid)) {
            throw new EnergyException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        if (ride.getEndTime() != null) {
            throw new EnergyException(ErrorCode.RIDE_ALREADY_ENDED);
        }

        ride.setEndTime(LocalDateTime.now());
        rideRepository.save(ride);

        long secondsUsed = Duration.between(ride.getStartTime().withSecond(0).withNano(0), ride.getEndTime().withSecond(0).withNano(0)).toSeconds();
        int distanceCovered = calculateDistance(secondsUsed);

        int burnedCalories = calculateCalories(distanceCovered);
        int generatedPower = calculateGeneratedPower(burnedCalories);

        User user = userRepository.findByUuid(userUuid)
            .orElseThrow(() -> new EnergyException(ErrorCode.USER_NOT_FOUND));

        int previousPowerGenerated = user.getTotPowerGen();               // 기존 누적 전력량
        int totalPowerGenerated = previousPowerGenerated + generatedPower; // 이번 이용을 포함한 총 전력량
        int convertedPoints = calculatePoints(totalPowerGenerated);       // 100당 10포인트 전환
        int finalPowerGenerated = totalPowerGenerated % 100;              // 포인트 전환 후 남은 전력량

        user.setTotPowerGen(finalPowerGenerated);
        user.setPoints(user.getPoints() + convertedPoints);
        userRepository.save(user);

        Bicycle bicycle = ride.getBicycle();
        int batteryStat = 0;

        if (bicycle != null) {
            bicycleService.updateBatteryStat(bicycle.getBicycleId(), generatedPower);
            batteryStat = bicycle.getBatteryStat();
            bicycle.setActive(false);
            bicycleRepository.save(bicycle);
        }

        ride.setDistance(distanceCovered);
        ride.setCaloriesBurned(burnedCalories);
        ride.setPowerGenerated(generatedPower);
        ride.setGeneratedPoints(convertedPoints);
        rideRepository.save(ride);


        return RideEndResponse.builder()
            .ride(ride)
            .userId(userUuid)
            .previousPowerGenerated(previousPowerGenerated)
            .powerGenerated(generatedPower)
            .totalPowerGenerated(totalPowerGenerated)
            .finalPowerGenerated(finalPowerGenerated)
            .convertedPoints(convertedPoints)
            .batteryStat(batteryStat)
            .build();
    }


    // 거리 계산 (1초마다 5m 증가)
    private int calculateDistance(long secondsUsed) {
        return (int) (secondsUsed) * 5;
    }

    // 칼로리 소모량 계산 (20m당 1kcal 소모)
    private int calculateCalories(int distanceCovered) {
        return distanceCovered / 20;
    }

    // 1.6kcal 당 1 전력량 증가
    private int calculateGeneratedPower(int burnedCalories) {
        return (int) (burnedCalories / 1.6);
    }

    // 100Wh = 10 포인트
    private int calculatePoints(int powerGenerated) {
        return (powerGenerated / 100) * 10;
    }


    // 이용 내역 출력
    public List<RideUsageHistory> getRideUsageHistory(String userUuid) {
        List<Ride> rides = rideRepository.findAllByUserIdOrderByStartRideDateDesc(userUuid);
        return rides.stream()
            .map(RideUsageHistory::new)
            .toList();
    }
}