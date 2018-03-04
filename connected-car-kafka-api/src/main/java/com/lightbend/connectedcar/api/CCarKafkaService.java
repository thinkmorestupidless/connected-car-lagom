package com.lightbend.connectedcar.api;

import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.broker.Topic;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.topic;

/**
 * The CCarKafkaService service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the CCarKafkaService.
 */
public interface CCarKafkaService extends Service {

    /**
     * This gets published to Kafka.
     */
    Topic<RawTelemetry> inputEvents();

    @Override
    default Descriptor descriptor() {
        return named("connectedcarkafka")
                .withTopics(
                        topic("connectedcar", this::inputEvents)
                )
                .withAutoAcl(true);
    }
}
