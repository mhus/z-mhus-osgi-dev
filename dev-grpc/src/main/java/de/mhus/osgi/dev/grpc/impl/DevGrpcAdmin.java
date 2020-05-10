package de.mhus.osgi.dev.grpc.impl;

public interface DevGrpcAdmin {

	void doStart(int port);

	void doShutdown();

}
