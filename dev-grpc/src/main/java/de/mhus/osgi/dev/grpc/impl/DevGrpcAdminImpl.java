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

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component(immediate = true)
public class DevGrpcAdminImpl implements DevGrpcAdmin {

    private LinkedList<ServiceRegistration<GrpcServer>> list = new LinkedList<>();

    @Activate
    public void doActivate() {
        try {
            doStart(8080); // start 8080 by default
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deactivate
    public void doDeactivate() {
        doShutdown();
    }

    @Override
    public void doShutdown() {
        list.forEach(i -> i.unregister());
        list.clear();
    }

    @Override
    public void doStart(int port) throws Exception {
        GrpcServerImpl service = new GrpcServerImpl();
        service.start(port);

        BundleContext ctx = FrameworkUtil.getBundle(DevGrpcAdminImpl.class).getBundleContext();

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put("port", port);
        ServiceRegistration<GrpcServer> reg =
                ctx.registerService(GrpcServer.class, service, properties);

        list.add(reg);
    }

    @Override
    public List<GrpcServer> list() {
        LinkedList<GrpcServer> out = new LinkedList<>();
        BundleContext ctx = FrameworkUtil.getBundle(DevGrpcAdminImpl.class).getBundleContext();

        for (ServiceRegistration<GrpcServer> reg : list)
            out.add(ctx.getService(reg.getReference()));

        return out;
    }
}
