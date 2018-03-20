package com.lightbend.connectedcar.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * The connectedcarlagom service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the ConnectedcarlagomService.
 */
public interface GreetingService extends Service {

    ServiceCall<NotUsed, String> greeting(String id);

    @Override
    default Descriptor descriptor() {
        return named("dummyservice")
                .withCalls(
                        pathCall("/api/greeting/", this::greeting)
                )
                .withAutoAcl(true);
    }
}
