package com.energy.tajo.ride.service;

import com.energy.tajo.bicycle.domain.Bicycle;
import com.energy.tajo.bicycle.repository.BicycleRepository;
import com.energy.tajo.bicycle.service.BicycleService;
import com.energy.tajo.global.exception.EnergyException;
import com.energy.tajo.global.exception.ErrorCode;
import com.energy.tajo.ride.domain.Ride;
import com.energy.tajo.ride.dto.response.RideUsageHistory;
import com.energy.tajo.ride.repository.RideRepository;
import com.energy.tajo.user.domain.User;
import com.energy.tajo.user.repository.UserRepository;
import com.energy.tajo.user.service.UserService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepository;
    private final BicycleService bicycleService;
    private final UserService userService;
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
    public Ride endRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new EnergyException(ErrorCode.RIDE_NOT_FOUND));

        LocalDateTime endTime = LocalDateTime.now();
        ride.setEndTime(endTime);

        // 총 이용 시간에 따른 거리 계산
        Duration duration = Duration.between(ride.getStartTime(), endTime);
        long secondsUsed = duration.getSeconds();
        int distanceCovered = calculateDistance(secondsUsed);
        ride.setDistance(distanceCovered);

        // 소모한 칼로리 계산
        int burnedCalories = calculateCalories(distanceCovered);
        ride.setCaloriesBurned(burnedCalories);

        // 생성된 전력량 계산
        int generatedPower = calculateGeneratedPower(burnedCalories);
        ride.setPowerGenerated(generatedPower);

        // 포인트 계산
        int generatedPoints = calculatePoints(generatedPower);
        ride.setGeneratedPoints(generatedPoints);

        // 자전거 배터리 상태 업데이트
        Bicycle bicycle = ride.getBicycle();
        if (bicycle != null) {
            bicycleService.updateBatteryStat(bicycle.getBicycleId(), generatedPower);
            bicycle.setActive(false);
            bicycleRepository.save(bicycle);
        }

        // 사용자 전력량 업데이트
        User user = userService.findUserById(ride.getUserId());
        if (user != null) {
            int updatedTotalPowerGen = user.getTotPowerGen() + generatedPower;
            user.setTotPowerGen(updatedTotalPowerGen);
            userRepository.save(user);
            userService.addPoints(user.getUuid(), generatedPoints);
        }

        return rideRepository.save(ride);
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

    // 10Wh = 1 포인트
    private int calculatePoints(int powerGenerated) {
        return powerGenerated / 10;
    }


    // 이용 내역 출력
    public List<RideUsageHistory> getRideUsageHistory(String userUuid) {
        List<Ride> rides = rideRepository.findAllByUserIdOrderByStartRideDateDesc(userUuid);
        return rides.stream()
            .map(RideUsageHistory::new)
            .collect(Collectors.toList());
    }
}