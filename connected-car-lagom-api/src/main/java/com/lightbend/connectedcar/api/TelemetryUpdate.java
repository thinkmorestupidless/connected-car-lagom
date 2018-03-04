package com.lightbend.connectedcar.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import lombok.Value;

import java.math.BigDecimal;

@Value
@JsonDeserialize
public final class TelemetryUpdate {

    private final int powerConsumption;

    private final int speed;

    private final int motorTemp;

    private final String driver;

    private final BigDecimal latitude;

    private final BigDecimal longitude;

    private final String status;

    private final int batteryLevel;

    @JsonCreator
    public TelemetryUpdate(int powerConsumption, int speed, int motorTemp, String driver, BigDecimal latitude, BigDecimal longitude, String status, int batteryLevel) {
        this.powerConsumption = Preconditions.checkNotNull(powerConsumption, "powerConsumption");
        this.speed = Preconditions.checkNotNull(speed, "speed");
        this.motorTemp = Preconditions.checkNotNull(motorTemp, "motorTemp");
        this.driver = Preconditions.checkNotNull(driver, "driver");
        this.latitude = Preconditions.checkNotNull(latitude, "latitude");
        this.longitude = Preconditions.checkNotNull(longitude, "longitude");
        this.status = Preconditions.checkNotNull(status, "status");
        this.batteryLevel = Preconditions.checkNotNull(batteryLevel, "batteryLevel");
    }
}
