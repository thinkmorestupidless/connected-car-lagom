package com.lightbend.connectedcar.impl;

import akka.Done;
import akka.NotUsed;
import akka.japi.Pair;
import com.lightbend.connectedcar.api.CCarService;
import com.lightbend.connectedcar.api.CCarSummary;
import com.lightbend.connectedcar.api.TelemetryUpdate;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import org.pcollections.HashTreePSet;
import org.pcollections.PCollection;
import org.pcollections.PSet;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation of the CCarService.
 */
public class CCarServiceImpl implements CCarService {

    private final PersistentEntityRegistry persistentEntityRegistry;

    private final CCarStreamRepository repository;

    @Inject
    public CCarServiceImpl(PersistentEntityRegistry persistentEntityRegistry, CCarStreamRepository repository) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        this.repository = repository;

        persistentEntityRegistry.register(CCarEntity.class);
    }

    public ServiceCall<String, String> echo() {
        return request -> CompletableFuture.completedFuture(request.toUpperCase());
    }

    public ServiceCall<NotUsed, Collection<CCarSummary>> summaries() {
        return request -> repository.summaries();
    }

    public ServiceCall<NotUsed, CCarSummary> summary(String id) {
        return request -> repository.summary(id);
    }

    @Override
    public ServiceCall<TelemetryUpdate, Done> telemetryUpdate(String id) {
        return request -> {
            PersistentEntityRef<CCarCommand> ref = persistentEntityRegistry.refFor(CCarEntity.class, id);
            return ref.ask(new CCarCommand.UpdateTelemetry(request.getPowerConsumption(), request.getSpeed(), request.getMotorTemp(), request.getDriver(), request.getLatitude(), request.getLongitude(), request.getStatus(), request.getBatteryLevel()));
        };
    }

    @Override
    public Topic<com.lightbend.connectedcar.api.CCarEvent> rawTelemetry() {
        // We want to publish all the shards of the hello event
        return TopicProducer.taggedStreamWithOffset(CCarEvent.TAG.allTags(), (tag, offset) ->
                // Load the event stream for the passed in shard tag
                persistentEntityRegistry.eventStream(tag, offset).map(eventAndOffset -> {

                    // Now we want to convert from the persisted event to the published event.
                    // Although these two events are currently identical, in future they may
                    // change and need to evolve separately, by separating them now we save
                    // a lot of potential trouble in future.
                    com.lightbend.connectedcar.api.CCarEvent eventToPublish;

                    if (eventAndOffset.first() instanceof CCarEvent.TelemetryChanged) {
                        CCarEvent.TelemetryChanged messageChanged = (CCarEvent.TelemetryChanged) eventAndOffset.first();
                        eventToPublish = new com.lightbend.connectedcar.api.CCarEvent.TelemetryChanged(
                                messageChanged.getCarId(),
                                messageChanged.getPowerConsumption(),
                                messageChanged.getSpeed(),
                                messageChanged.getMotorTemp(),
                                messageChanged.getDriver(),
                                messageChanged.getLatitude(),
                                messageChanged.getLongitude(),
                                messageChanged.getStatus(),
                                messageChanged.getBatteryLevel()
                        );
                    } else {
                        throw new IllegalArgumentException("Unknown event: " + eventAndOffset.first());
                    }

                    // We return a pair of the translated event, and its offset, so that
                    // Lagom can track which offsets have been published.
                    return Pair.create(eventToPublish, eventAndOffset.second());
                })
        );
    }
}
