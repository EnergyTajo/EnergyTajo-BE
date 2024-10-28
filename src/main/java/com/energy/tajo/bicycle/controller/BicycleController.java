package com.energy.tajo.bicycle.controller;

import com.energy.tajo.bicycle.dto.response.BicycleResponse;
import com.energy.tajo.bicycle.service.BicycleService;
import com.energy.tajo.ride.domain.Ride;
import com.energy.tajo.ride.dto.response.RideEndResponse;
import com.energy.tajo.ride.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qr_bicycle")
public class BicycleController {

    private final BicycleService bicycleService;
    private final RideService rideService;

    public BicycleController(BicycleService bicycleService, RideService rideService) {
        this.bicycleService = bicycleService;
        this.rideService = rideService;
    }

    // QR 코드 인식 후 자전거 정보 조회
    @GetMapping("/{bicycleId}")
    public ResponseEntity<BicycleResponse> getBikeInfo(@PathVariable String bicycleId) {

        BicycleResponse responseDto = bicycleService.getBicycleInfo(bicycleId);
        return ResponseEntity.ok(responseDto);
    }

    // 자전거 이용 시작
    @PostMapping("/start/{bicycleId}")
    public Ride startRide(@PathVariable String bicycleId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userUuid = (String) authentication.getPrincipal();
        return rideService.startRide(bicycleId, userUuid);
    }


    // 자전거 이용 종료
    @PostMapping("/end/{rideId}")
    public ResponseEntity<RideEndResponse> endRide(@PathVariable Long rideId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userUuid = (String) authentication.getPrincipal();
        RideEndResponse responseDto = rideService.endRide(rideId, userUuid);
        return ResponseEntity.ok(responseDto);
    }
}