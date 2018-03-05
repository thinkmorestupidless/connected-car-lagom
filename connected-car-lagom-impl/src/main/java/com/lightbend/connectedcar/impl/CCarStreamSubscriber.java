package com.lightbend.connectedcar.impl;

import akka.Done;
import akka.stream.javadsl.Flow;
import com.lightbend.connectedcar.api.CCarKafkaService;
import com.lightbend.connectedcar.api.RawTelemetry;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

/**
 * This subscribes to the ConnectedcarlagomService event stream.
 */
public class CCarStreamSubscriber {

    @Inject
    public CCarStreamSubscriber(CCarKafkaService kafka, PersistentEntityRegistry persistentEntityRegistry) {

        // Create a subscriber
        kafka.inputEvents().subscribe()
                .atLeastOnce(
                        Flow.<RawTelemetry>create().mapAsync(1, event -> {

                            String id = event.getCarId();
                            int powerConsumption = 0;

                            if (event.getPowerConsumption() != null && !event.getPowerConsumption().isEmpty()) {
                                powerConsumption = Integer.parseInt(event.getPowerConsumption());
                            }

                            int speed = 0;

                            if (event.getSpeed() != null && !event.getSpeed().isEmpty()) {
                                speed = Integer.parseInt(event.getSpeed());
                            }

                            int motorTemp = 0;

                            if (event.getMotorTemp() != null && !event.getMotorTemp().isEmpty()) {
                                motorTemp = Integer.parseInt(event.getMotorTemp());
                            }

                            int batteryLevel = 0;

                            if (event.getBatteryLevel() != null && !event.getBatteryLevel().isEmpty()) {
                                batteryLevel = Integer.parseInt(event.getBatteryLevel());
                            }

                            BigDecimal latitude = BigDecimal.ZERO;
                            BigDecimal longitude = BigDecimal.ZERO;

                            if (event.getLocation() != null && !event.getLocation().isEmpty()) {
                                String[] latlong = event.getLocation().split(",");
                                if (latlong.length == 2) {

                                    if (latlong[0] != null && !latlong[0].isEmpty()) {
                                        latitude = new BigDecimal(latlong[0].replaceAll(" ", ""));
                                    }

                                    if (latlong[1] != null && !latlong[1].isEmpty()) {
                                        longitude = new BigDecimal(latlong[1].replaceAll(" ", ""));
                                    }
                                }
                            }

                            CCarCommand.UpdateTelemetry cmd = new CCarCommand.UpdateTelemetry(powerConsumption, speed, motorTemp, event.getDriver(), latitude, longitude, event.getStatus(), batteryLevel);

                            PersistentEntityRef<CCarCommand> ref = persistentEntityRegistry.refFor(CCarEntity.class, id);

                            ref.ask(cmd);

                            return CompletableFuture.completedFuture(Done.getInstance());
                        })
                );
    }
}
