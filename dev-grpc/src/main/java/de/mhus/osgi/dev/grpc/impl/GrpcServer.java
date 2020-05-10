package de.mhus.osgi.dev.grpc.impl;

public interface GrpcServer {

	void shutdown();
	
	void start(int port) throws Exception;
	
}
