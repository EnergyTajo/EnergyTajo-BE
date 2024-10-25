package com.energy.tajo.ride.controller;

import com.energy.tajo.ride.dto.response.RideUsageHistory;
import com.energy.tajo.ride.service.RideService;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ride")
public class RideController {
    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // 탑승 이용 내역
    @GetMapping
    public List<RideUsageHistory> getRideUsageHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userUuid = (String) authentication.getPrincipal();
        return rideService.getRideUsageHistory(userUuid);
    }
}