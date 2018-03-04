package com.lightbend.connectedcar.impl;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

import java.math.BigDecimal;

/**
 * This interface defines all the commands that the CCarEntity supports.
 * <p>
 * By convention, the commands should be inner classes of the interface, which
 * makes it simple to get a complete picture of what commands an entity
 * supports.
 */
public interface CCarCommand extends Jsonable {

    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
    final class UpdateTelemetry implements CCarCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {

        private final int powerConsumption;

        private final int speed;

        private final int motorTemp;

        private final String driver;

        private final BigDecimal latitude;

        private final BigDecimal longitude;

        private final String status;

        private final int batteryLevel;

        @JsonCreator
        UpdateTelemetry(int powerConsumption, int speed, int motorTemp, String driver, BigDecimal latitude, BigDecimal longitude, String status, int batteryLevel) {
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
