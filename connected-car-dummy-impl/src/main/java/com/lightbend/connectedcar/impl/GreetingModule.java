package com.lightbend.connectedcar.impl;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.lightbend.connectedcar.api.GreetingService;
import com.lightbend.lagom.javadsl.api.ServiceLocator;
import com.lightbend.lagom.javadsl.client.ConfigurationServiceLocator;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import play.Environment;

/**
 * The module that binds the ConnectedcarlagomService so that it can be served.
 */
public class GreetingModule extends AbstractModule implements ServiceGuiceSupport {

    private final Environment environment;

    @Inject
    public GreetingModule(Environment environment) {
        this.environment = environment;
    }

    @Override
    protected void configure() {
        if (environment.isProd()) {
            bind(ServiceLocator.class).to(ConfigurationServiceLocator.class);
        }

        bindService(GreetingService.class, GreetingServiceImpl.class);
    }
}
