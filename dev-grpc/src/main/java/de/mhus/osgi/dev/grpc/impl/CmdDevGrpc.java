/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
                            + "  client <name>\n"
                            + "  list",
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
            int p = parameters == null ? port : MCast.toint(parameters[0], port);
            M.l(DevGrpcAdmin.class).doStart(p);
            System.out.println("Started on " + p);
        } else if (cmd.equals("shutdown")) {
            M.l(DevGrpcAdmin.class).doShutdown();
            System.out.println("OK");
        } else if (cmd.equals("client")) {
            doClientCall();
        } else if (cmd.equals("list")) {
            for (GrpcServer inst : M.l(DevGrpcAdmin.class).list()) {
                System.out.println(inst.getServer());
            }
        }
        return null;
    }

    private void doClientCall() {
        ManagedChannel channel = OkHttpChannelBuilder.forAddress(host, port).usePlaintext().build();

        //		ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
        //		        .usePlaintext()
        //		        .build();

        GreetingServiceGrpc.GreetingServiceBlockingStub stub =
                GreetingServiceGrpc.newBlockingStub(channel);

        int cnt = 1;
        if (parameters != null && parameters.length > 1) cnt = M.to(parameters[1], 1);
        for (int i = 0; i < cnt; i++) {
            HelloResponse helloResponse =
                    stub.greeting(
                            HelloRequest.newBuilder()
                                    .setName(parameters[0] + "-" + i)
                                    .setAge(18)
                                    .setSentiment(Sentiment.HAPPY)
                                    .build());

            System.out.println("Client: " + helloResponse);
        }
        channel.shutdown();
    }
}
