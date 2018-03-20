package com.lightbend.connectedcar.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;

import java.util.Collection;

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

    // curl -X POST -H "Content-Type: text/plain" -d  "hello world" http://localhost:9000/api/echo

    ServiceCall<String, String> echo();

    ServiceCall<NotUsed, Collection<CCarSummary>> summaries();

    ServiceCall<NotUsed, CCarSummary> summary(String id);

    /**
     * Example:
     * curl -H "Content-Type: application/json" -X POST -d '{"powerConsumption":70,"speed":30,"motorTemp":91,"driver":"Heidi Decker","latitude":37.851040,"longitude":-122.250970,"status":"HEALTHY","batteryLevel":98}' http://localhost:9000/api/telemetry/12695
     */
    ServiceCall<TelemetryUpdate, Done> telemetryUpdate(String id);

    /**
     * This gets published to Kafka.
     */
    Topic<CCarEvent> rawTelemetry();

    @Override
    default Descriptor descriptor() {
        return named("connectedcarlagom")
                .withCalls(
                        pathCall("/api/echo", this::echo),
                        pathCall("/api/hello/", this::summaries),
                        pathCall("/api/hello/:id", this::summary),
                        pathCall("/api/telemetry/:id", this::telemetryUpdate)
                )
                .withTopics(
                        topic("ccar-telemetry", this::rawTelemetry)
                )
                .withAutoAcl(true);
    }
}
