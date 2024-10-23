package com.energy.tajo.bicycle.controller;

import com.energy.tajo.auth.jwt.JwtTokenProvider;
import com.energy.tajo.bicycle.dto.response.BicycleResponseDto;
import com.energy.tajo.bicycle.service.BicycleService;
import com.energy.tajo.ride.domain.Ride;
import com.energy.tajo.ride.dto.response.RideResponseDto;
import com.energy.tajo.ride.service.RideService;
import com.energy.tajo.user.domain.User;
import com.energy.tajo.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qr_bicycle")
public class BicycleController {

    private final BicycleService bicycleService;
    private final RideService rideService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public BicycleController(BicycleService bicycleService, RideService rideService, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.bicycleService = bicycleService;
        this.rideService = rideService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // QR 코드 인식 후 자전거 정보 조회
    @GetMapping("/{bicycleId}")
    public ResponseEntity<BicycleResponseDto> getBikeInfo(@PathVariable String bicycleId) {
        BicycleResponseDto responseDto = bicycleService.getBicycleInfo(bicycleId);
        return ResponseEntity.ok(responseDto);
    }

    // 자전거 이용 시작
    @PostMapping("/start/{bicycleId}")
    public Ride startRide(@PathVariable String bicycleId, @RequestHeader("Authorization") String token) {
        String userUuid = jwtTokenProvider.extractUuidFromAccessToken(token);
        return rideService.startRide(bicycleId, userUuid);
    }

    // 자전거 이용 종료
    @PostMapping("/end/{rideId}")
    public ResponseEntity<RideResponseDto> endRide(@PathVariable Long rideId, @RequestHeader("Authorization") String token) {
        String userUuid = jwtTokenProvider.extractUuidFromAccessToken(token);
        User user = userService.findUserById(userUuid);

        Ride ride = rideService.endRide(rideId);
        RideResponseDto responseDto = new RideResponseDto(ride, user);
        return ResponseEntity.ok(responseDto);
    }
}