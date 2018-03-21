package com.lightbend.connectedcar.impl;

import akka.actor.ActorSystem;
import akka.management.AkkaManagement;
import akka.management.cluster.bootstrap.ClusterBootstrap;
import com.google.inject.Inject;

public class Bootstrapper {

    @Inject
    public Bootstrapper(ActorSystem system) {

        System.out.println("Starting cluster bootstrap");

        AkkaManagement.get(system).start();
        ClusterBootstrap.get(system).start();
    }
}
