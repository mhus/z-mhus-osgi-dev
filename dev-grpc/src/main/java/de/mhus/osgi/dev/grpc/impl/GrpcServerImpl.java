package de.mhus.osgi.dev.grpc.impl;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServerImpl implements GrpcServer {

    private Server server;

    @Override
    public void shutdown() {
        server.shutdownNow();
    }

    @Override
    public void start(int port) throws Exception {
        server = ServerBuilder.forPort(port).addService(new GreetingServiceImpl()).build();

        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started!");
    }

    @Override
    public Server getServer() {
        return server;
    }
}
