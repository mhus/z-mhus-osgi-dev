package de.mhus.osgi.dev.grpc.impl;

import io.grpc.Server;

public interface GrpcServer {

	void shutdown();
	
	void start(int port) throws Exception;

	Server getServer();
	
}
