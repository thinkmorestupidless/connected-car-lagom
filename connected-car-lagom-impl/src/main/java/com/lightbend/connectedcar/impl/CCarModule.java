package com.lightbend.connectedcar.impl;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.lightbend.connectedcar.api.CCarKafkaService;
import com.lightbend.connectedcar.api.CCarService;
import com.lightbend.lagom.javadsl.api.ServiceLocator;
import com.lightbend.lagom.javadsl.dns.DnsServiceLocator;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import play.Environment;

/**
 * The module that binds the ConnectedcarlagomService so that it can be served.
 */
public class CCarModule extends AbstractModule implements ServiceGuiceSupport {

    private final Environment environment;

    @Inject
    public CCarModule(Environment environment) {
        this.environment = environment;
    }

    @Override
    protected void configure() {
        if (environment.isProd()) {
            bind(ServiceLocator.class).to(DnsServiceLocator.class);
        }

        bind(Bootstrapper.class).asEagerSingleton();

        bindService(CCarService.class, CCarServiceImpl.class);

        bindClient(CCarKafkaService.class);

        // Bind the subscriber eagerly to ensure it starts up
        bind(CCarStreamSubscriber.class).asEagerSingleton();
    }
}
