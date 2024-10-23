package com.energy.tajo.bicycle.dto.response;

import com.energy.tajo.bicycle.domain.Bicycle;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BicycleResponseDto {
    private String bicycleId;
    private boolean active;
    private int batteryStat;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public BicycleResponseDto(Bicycle bicycle) {
        this.bicycleId = bicycle.getBicycleId();
        this.active = bicycle.isActive();
        this.batteryStat = bicycle.getBatteryStat();
        this.latitude = bicycle.getLatitude();
        this.longitude = bicycle.getLongitude();
    }
}