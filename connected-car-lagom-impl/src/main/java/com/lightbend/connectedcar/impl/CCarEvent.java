package com.lightbend.connectedcar.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

import java.math.BigDecimal;

/**
 * This interface defines all the events that the ConnectedcarlagomEntity supports.
 * <p>
 * By convention, the events should be inner classes of the interface, which
 * makes it simple to get a complete picture of what events an entity has.
 */
public interface CCarEvent extends Jsonable, AggregateEvent<CCarEvent> {
    /**
     * Tags are used for getting and publishing streams of events. Each event
     * will have this tag, and in this case, we are partitioning the tags into
     * 4 shards, which means we can have 4 concurrent processors/publishers of
     * events.
     */
    AggregateEventShards<CCarEvent> TAG = AggregateEventTag.sharded(CCarEvent.class, 4);

    /**
     * An event that represents a change in greeting message.
     */
    @SuppressWarnings("serial")
    @Value
    @JsonDeserialize
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
        TelemetryChanged(String carId, int powerConsumption, int speed, int motorTemp, String driver, BigDecimal latitude, BigDecimal longitude, String status, int batteryLevel) {
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

    @Override
    default AggregateEventTagger<CCarEvent> aggregateTag() {
        return TAG;
    }
}
