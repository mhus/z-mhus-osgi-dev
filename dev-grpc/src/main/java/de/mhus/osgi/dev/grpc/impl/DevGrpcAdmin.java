package de.mhus.osgi.dev.grpc.impl;

import java.util.List;

public interface DevGrpcAdmin {

    void doStart(int port) throws Exception;

    void doShutdown();

    List<GrpcServer> list();
}
