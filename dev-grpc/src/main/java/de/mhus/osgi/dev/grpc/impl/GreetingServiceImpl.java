package de.mhus.osgi.dev.grpc.impl;

import de.mhus.osgi.dev.grpc.impl.api.GreetingServiceGrpc.GreetingServiceImplBase;
import de.mhus.osgi.dev.grpc.impl.api.HelloRequest;
import de.mhus.osgi.dev.grpc.impl.api.HelloResponse;
import io.grpc.stub.StreamObserver;

public class GreetingServiceImpl extends GreetingServiceImplBase {

	@Override
    public void greeting(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
      System.out.println(request);

      String greeting = "Hello there, " + request.getName();

      HelloResponse response = HelloResponse.newBuilder().setGreeting(greeting).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
	
}
