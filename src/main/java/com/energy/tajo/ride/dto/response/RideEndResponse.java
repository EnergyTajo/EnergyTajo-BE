package com.energy.tajo.ride.dto.response;

import com.energy.tajo.ride.domain.Ride;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import java.time.format.DateTimeFormatter;

@Getter
public class RideEndResponse {

    private final String userId;
    private final String bicycleId;
    private final String startRideDate;           // 시작 날짜 (yyyy.MM.dd)
    private final String startTime;               // 시작 시간 (HH:mm)
    private final String endTime;                 // 종료 시간 (HH:mm)
    private final String duration;                // 이용 시간 (xx시간 xx분)
    private final int distance;
    private final int caloriesBurned;
    private final int powerGenerated;             // 라이딩 시 생성된 전력량
    private final int previousPowerGenerated;     // 이전에 누적된 전력량 (기존 tot_power_gen 값)
    private final int totalPowerGenerated;        // 기존 전력량 + 이번 이용 전력량
    private final int finalPowerGenerated;        // 포인트 전환 후 남은 전력량 (user.tot_power_gen에 저장될 값)
    private final int convertedPoints;            // 전환된 포인트
    private final int batteryStat;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Builder
    public RideEndResponse(Ride ride, String userId, int previousPowerGenerated, int powerGenerated, int totalPowerGenerated, int finalPowerGenerated, int convertedPoints, int batteryStat) {
        this.userId = userId;
        this.bicycleId = ride.getBicycle().getBicycleId();
        this.startRideDate = ride.getStartRideDate().format(DATE_FORMATTER);
        this.startTime = ride.getStartTime().format(TIME_FORMATTER);
        this.endTime = ride.getEndTime().format(TIME_FORMATTER);
        this.duration = formatDuration(ride.getStartTime(), ride.getEndTime());
        this.distance = ride.getDistance();
        this.caloriesBurned = ride.getCaloriesBurned();
        this.powerGenerated = powerGenerated;
        this.previousPowerGenerated = previousPowerGenerated;
        this.totalPowerGenerated = totalPowerGenerated;
        this.finalPowerGenerated = finalPowerGenerated;
        this.convertedPoints = convertedPoints;
        this.batteryStat = batteryStat;
    }

    // 이용 시간 형식 변환
    private String formatDuration(LocalDateTime start, LocalDateTime end) {
        long totalMinutes = Duration.between(start.withSecond(0).withNano(0), end.withSecond(0).withNano(0)).toMinutes();
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return hours + "시간 " + minutes + "분";
    }
}
