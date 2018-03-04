package com.lightbend.connectedcar.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import lombok.Value;

@JsonDeserialize
@Value
public class RawTelemetry {

    private final String powerConsumption;

    private final String speed;

    private final String motorTemp;

    private final String carId;

    private final String driver;

    private final String location;

    private final String id;

    private final String status;

    private final String timestamp;

    private final String batteryLevel;

    @JsonCreator
    public RawTelemetry(@JsonProperty("powerconsumption") String powerConsumption,
                        @JsonProperty("speed") String speed,
                        @JsonProperty("motortemp") String motorTemp,
                        @JsonProperty("carid") String carId,
                        @JsonProperty("driver") String driver,
                        @JsonProperty("location") String location,
                        @JsonProperty("id") String id,
                        @JsonProperty("status") String status,
                        @JsonProperty("event_timestamp") String timestamp,
                        @JsonProperty("batterylevel") String batteryLevel) {
        this.powerConsumption = powerConsumption;
        this.speed = speed;
        this.motorTemp = motorTemp;
        this.carId = carId;
        this.driver = driver;
        this.location = location;
        this.id = id;
        this.status = status;
        this.timestamp = timestamp;
        this.batteryLevel = batteryLevel;
    }
}
