package com.lightbend.connectedcar.impl;

import akka.actor.ActorSystem;
import akka.management.AkkaManagement;
import akka.management.cluster.bootstrap.ClusterBootstrap;
import com.google.inject.Inject;
import play.Environment;

public class Bootstrapper {

    @Inject
    public Bootstrapper(Environment environment, ActorSystem system) {

//        System.out.println("Starting cluster bootstrap - " + environment.mode());
        System.out.println("Starting cluster bootstrap");

        if (environment.isProd()) {

            AkkaManagement.get(system).start();
            ClusterBootstrap.get(system).start();
        }
    }
}
