package com.lightbend.connectedcar.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.Optional;

public class CCarEntity extends PersistentEntity<CCarCommand, CCarEvent, CCarState> {
    /**
     * An entity can define different behaviours for different states, but it will
     * always start with an initial behaviour. This entity only has one behaviour.
     */
    @Override
    public Behavior initialBehavior(Optional<CCarState> snapshotState) {
        /*
         * Behaviour is defined using a behaviour builder. The behaviour builder
         * starts with a state, if this entity supports snapshotting (an
         * optimisation that allows the state itself to be persisted to combine many
         * events into one), then the passed in snapshotState may have a value that
         * can be used.
         *
         * Otherwise, the default state is to use the Hello greeting.
         */
        BehaviorBuilder b = newBehaviorBuilder(
                snapshotState.orElse(CCarState.empty())
        );

        /*
         * Command handler for the UseGreetingMessage command.
         */
        b.setCommandHandler(CCarCommand.UpdateTelemetry.class, (cmd, ctx) ->
                // In response to this command, we want to first persist it as a
                // GreetingMessageChanged event
                ctx.thenPersist(new CCarEvent.TelemetryChanged(entityId(), cmd.getPowerConsumption(), cmd.getSpeed(), cmd.getMotorTemp(), cmd.getDriver(), cmd.getLatitude(), cmd.getLongitude(), cmd.getStatus(), cmd.getBatteryLevel()),
                        // Then once the event is successfully persisted, we respond with done.
                        evt -> ctx.reply(Done.getInstance())
                )
        );

        /*
         * Event handler for the GreetingMessageChanged event.
         */
        b.setEventHandler(CCarEvent.TelemetryChanged.class,
                // We simply update the current state to use the greeting message from
                // the event.
                evt -> new CCarState(evt.getPowerConsumption(), evt.getSpeed(), evt.getMotorTemp(), evt.getDriver(), evt.getLatitude(), evt.getLongitude(), evt.getStatus(), evt.getBatteryLevel())
        );

        /*
         * We've defined all our behaviour, so build and return it.
         */
        return b.build();
    }
}
