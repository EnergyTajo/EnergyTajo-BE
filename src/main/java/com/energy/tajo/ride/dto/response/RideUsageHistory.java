package com.energy.tajo.ride.dto.response;

import com.energy.tajo.ride.domain.Ride;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RideUsageHistory {
    private String bicycleId;
    private String startRideDate;
    private String duration;
    private int convertedPoints;

    public RideUsageHistory(Ride ride) {

        this.bicycleId = ride.getBicycle().getBicycleId();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.startRideDate = ride.getStartRideDate().format(dateFormatter);
        this.duration = formatDuration(ride.getStartTime(), ride.getEndTime());
        this.convertedPoints = ride.getGeneratedPoints();
    }

    private String formatDuration(LocalDateTime start, LocalDateTime end) {

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String startTimeStr = start.format(timeFormatter);
        String endTimeStr = end.format(timeFormatter);
        long totalMinutes = java.time.Duration.between(start.withSecond(0).withNano(0), end.withSecond(0).withNano(0)).toMinutes();
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        return startTimeStr + "~" + endTimeStr + "(" + (hours > 0 ? hours + "시간 " : "") + minutes + "분)";
    }
}