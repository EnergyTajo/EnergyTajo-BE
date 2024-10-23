package com.energy.tajo.ride.domain;

import com.energy.tajo.bicycle.domain.Bicycle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Ride {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bicycle_id")
    private Bicycle bicycle;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "user_id")
    private String userId;

    @Column(nullable = false, name="start_ride_date")
    private LocalDate startRideDate;

    @Column(nullable = false, name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Integer distance = 0;

    @Column(name = "power_generated", nullable = false)
    private Integer powerGenerated = 0;

    @Column(name = "calories_burned", nullable = false)
    private Integer caloriesBurned = 0;

    @Column(name = "generated_points", nullable = false)
    private int generatedPoints = 0;
}