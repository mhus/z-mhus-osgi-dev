package de.mhus.osgi.dev.grpc.impl;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MCast;
import de.mhus.osgi.api.karaf.AbstractCmd;
import de.mhus.osgi.dev.grpc.impl.api.GreetingServiceGrpc;
import de.mhus.osgi.dev.grpc.impl.api.HelloRequest;
import de.mhus.osgi.dev.grpc.impl.api.HelloResponse;
import de.mhus.osgi.dev.grpc.impl.api.Sentiment;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.okhttp.OkHttpChannelBuilder;

// https://github.com/saturnism/grpc-by-example-java
// https://github.com/stackleader/karaf-grpc

@Command(scope = "mhus", name = "dev-grpc", description = "gRPC tests")
@Service
public class CmdDevGrpc extends AbstractCmd {

    @Argument(
            index = 0,
            name = "cmd",
            required = true,
            description =
                    "Command to execute\n"
                    + "  start <port>\n"
                    + "  shutdown\n"
                    + "  client <name>"
            ,
            multiValued = false)
    String cmd;

    @Argument(
            index = 1,
            name = "paramteters",
            required = false,
            description = "Parameters",
            multiValued = true)
    String[] parameters;
    
    @Option(name = "--host", description = "Host", required = false, multiValued = false)
    String host = "";

    @Option(name = "--port", description = "Port", required = false, multiValued = false)
    int port = 8080;
    
    @Override
    public Object execute2() throws Exception {
        
    	if (cmd.equals("start")) {
    		M.l(DevGrpcAdmin.class).doStart(parameters == null ? port : MCast.toint(parameters[0], port));
    		System.out.println("OK");
    	} else
    	if (cmd.equals("shutdown")) {
    		M.l(DevGrpcAdmin.class).doShutdown();
    		System.out.println("OK");
    	} else
    	if (cmd.equals("client")) {
    		doClientCall();
    	}
        return null;
    }

	private void doClientCall() {
		ManagedChannel channel = OkHttpChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
		
//		ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
//		        .usePlaintext()
//		        .build();

		    GreetingServiceGrpc.GreetingServiceBlockingStub stub =
		        GreetingServiceGrpc.newBlockingStub(channel);

		    HelloResponse helloResponse = stub.greeting(
		        HelloRequest.newBuilder()
		            .setName(parameters[0])
		            .setAge(18)
		            .setSentiment(Sentiment.HAPPY)
		            .build());

		    System.out.println(helloResponse);

		    channel.shutdown();		
	}
    

    
}
