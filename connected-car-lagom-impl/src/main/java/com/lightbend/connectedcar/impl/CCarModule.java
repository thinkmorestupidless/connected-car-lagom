package com.lightbend.connectedcar.impl;

import com.google.inject.AbstractModule;
import com.lightbend.connectedcar.api.CCarKafkaService;
import com.lightbend.connectedcar.api.CCarService;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

/**
 * The module that binds the ConnectedcarlagomService so that it can be served.
 */
public class CCarModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(CCarService.class, CCarServiceImpl.class);

        bindClient(CCarKafkaService.class);

        // Bind the subscriber eagerly to ensure it starts up
        bind(CCarStreamSubscriber.class).asEagerSingleton();
    }
}
