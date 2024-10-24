package com.energy.tajo.ride.controller;

import com.energy.tajo.auth.jwt.JwtTokenProvider;
import com.energy.tajo.ride.dto.response.RideUsageHistory;
import com.energy.tajo.ride.service.RideService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ride")
public class RideController {
    private final RideService rideService;
    private final JwtTokenProvider jwtTokenProvider;

    public RideController(RideService rideService, JwtTokenProvider jwtTokenProvider) {
        this.rideService = rideService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping
    public List<RideUsageHistory> getRideUsageHistory(@RequestHeader("Authorization") String token) {
        String userUuid = jwtTokenProvider.extractUuidFromAccessToken(token);
        return rideService.getRideUsageHistory(userUuid);
    }
}
