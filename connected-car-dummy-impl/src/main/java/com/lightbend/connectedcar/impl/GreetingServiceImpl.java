package com.lightbend.connectedcar.impl;

import akka.NotUsed;
import com.lightbend.connectedcar.api.GreetingService;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation of the CCarService.
 */
public class GreetingServiceImpl implements GreetingService {

    @Inject
    public GreetingServiceImpl() {

    }

    public ServiceCall<NotUsed, String> greeting(String id) {
        return request -> CompletableFuture.completedFuture("Hello, " + id);
    }
}
