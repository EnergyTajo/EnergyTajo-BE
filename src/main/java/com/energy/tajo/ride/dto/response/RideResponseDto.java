package com.energy.tajo.ride.dto.response;

import com.energy.tajo.ride.domain.Ride;
import com.energy.tajo.user.domain.User;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RideResponseDto {
    private String bicycleId;
    private String startRideDate;
    private String startTime;
    private String endTime;
    private String duration;
    private int distance;
    private int caloriesBurned;
    private int powerGenerated;
    private int convertedPoints;
    private int totalPowerGenerated;
    private int previousPowerGenerated;
    private String userName;

    public RideResponseDto(Ride ride, User user) {
        this.bicycleId = ride.getBicycle().getBicycleId();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.startRideDate = ride.getStartRideDate().format(dateFormatter);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        this.startTime = ride.getStartTime().format(timeFormatter);
        this.endTime = ride.getEndTime() != null ? ride.getEndTime().format(timeFormatter) : null;

        this.duration = formatDuration(ride.getStartTime(), ride.getEndTime());
        this.distance = ride.getDistance();
        this.caloriesBurned = ride.getCaloriesBurned();
        this.powerGenerated = ride.getPowerGenerated();
        this.convertedPoints = ride.getGeneratedPoints();
        this.previousPowerGenerated = user.getTotPowerGen();
        this.totalPowerGenerated = calculateTotalPowerGenerated(user.getTotPowerGen(), ride.getPowerGenerated());
        this.userName = user.getName();
    }

    // 이용 시간 계산
    private String formatDuration(LocalDateTime start, LocalDateTime end) {
        long totalMinutes = java.time.Duration.between(start.withSecond(0).withNano(0), end.withSecond(0).withNano(0)).toMinutes();
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return hours + "시간 " + minutes + "분";
    }

    // 누적 전력량 계산
    private int calculateTotalPowerGenerated(int userTotalPowerGen, int ridePowerGenerated) {
        return userTotalPowerGen + ridePowerGenerated;
    }
}