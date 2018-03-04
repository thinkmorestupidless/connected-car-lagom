package com.lightbend.connectedcar.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Preconditions;
import lombok.Value;

import java.math.BigDecimal;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CCarEvent.TelemetryChanged.class, name = "telemetry-changed")
})
public interface CCarEvent {

    @Value
    final class TelemetryChanged implements CCarEvent {

        private final String carId;

        private final int powerConsumption;

        private final int speed;

        private final int motorTemp;

        private final String driver;

        private final BigDecimal latitude;

        private final BigDecimal longitude;

        private final String status;

        private final int batteryLevel;

        @JsonCreator
        public TelemetryChanged(String carId, int powerConsumption, int speed, int motorTemp, String driver, BigDecimal latitude, BigDecimal longitude, String status, int batteryLevel) {
            this.carId = Preconditions.checkNotNull(carId, "carId");
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
}
