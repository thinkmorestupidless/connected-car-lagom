package com.lightbend.connectedcar.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.Value;

import java.math.BigDecimal;

/**
 * The state for the {@link CCarEntity} entity.
 */
@SuppressWarnings("serial")
@Value
@JsonDeserialize
public final class CCarState implements CompressedJsonable {

    private final int powerConsumption;

    private final int speed;

    private final int motorTemp;

    private final String driver;

    private final BigDecimal latitude;

    private final BigDecimal longitude;

    private final String status;

    private final int batteryLevel;

    @JsonCreator
    CCarState(int powerConsumption, int speed, int motorTemp, String driver, BigDecimal latitude, BigDecimal longitude, String status, int batteryLevel) {
        this.powerConsumption = Preconditions.checkNotNull(powerConsumption, "powerConsumption");
        this.speed = Preconditions.checkNotNull(speed, "speed");
        this.motorTemp = Preconditions.checkNotNull(motorTemp, "motorTemp");
        this.driver = Preconditions.checkNotNull(driver, "driver");
        this.latitude = Preconditions.checkNotNull(latitude, "latitude");
        this.longitude = Preconditions.checkNotNull(longitude, "longitude");
        this.status = Preconditions.checkNotNull(status, "status");
        this.batteryLevel = Preconditions.checkNotNull(batteryLevel, "batteryLevel");
    }

    public static CCarState empty() {
        return new CCarState(0, 0, 0, "", BigDecimal.ZERO, BigDecimal.ZERO, "", 0);
    }
}
