package com.lightbend.connectedcar.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.api.broker.kafka.KafkaProperties;

import static com.lightbend.lagom.javadsl.api.Service.*;

/**
 * The connectedcarlagom service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the ConnectedcarlagomService.
 */
public interface CCarService extends Service {
//    /**
//     * Example:
//     * curl http://localhost:9000/api/hello/Alice
//     */
//    ServiceCall<NotUsed, String> hello(String id);
//
//    /**
//     * Example:
//     * curl -H "Content-Type: application/json" -X POST -d '{"message": "Hi"}' http://localhost:9000/api/hello/Alice
//     */
//    ServiceCall<TelemetryUpdate, Done> useGreeting(String id);

    ServiceCall<TelemetryUpdate, Done> telemetryUpdate(String id);

    /**
     * This gets published to Kafka.
     */
    Topic<CCarEvent> rawTelemetry();

    @Override
    default Descriptor descriptor() {
        return named("connectedcarlagom")
                .withCalls(
//                        pathCall("/api/hello/:id", this::hello),
//                        pathCall("/api/hello/:id", this::useGreeting)
                        pathCall("/api/telemetry/:id", this::telemetryUpdate)
                )
                .withTopics(
                        topic("connectedcar", this::rawTelemetry)
                )
                .withAutoAcl(true);
    }
}
