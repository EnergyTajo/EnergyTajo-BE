package com.energy.tajo.bicycle.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Bicycle {

    @Id
    @Column(nullable = false,  columnDefinition = "CHAR(30)", name="bicycle_id")
    private String bicycleId;

    // 위도
    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal latitude;

    // 경도
    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal longitude;

    @Column(nullable = false, name="battery_stat")
    private int batteryStat;

    @Column(nullable = false, name="power_generated_total")
    private int powerGeneratedTotal;

    @Column(nullable = false, name="last_maintenance")
    private Date lastMaintenance;

    @Column(nullable = false)
    private boolean active;
}
