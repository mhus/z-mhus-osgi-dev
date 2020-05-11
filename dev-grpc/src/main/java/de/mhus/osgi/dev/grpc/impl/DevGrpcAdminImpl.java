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

@Component
public class DevGrpcAdminImpl implements DevGrpcAdmin {

	private LinkedList<ServiceRegistration<GrpcServer>> list = new LinkedList<>();

	@Activate
	public void doActivate() {
		
	}
	
	@Deactivate
	public void doDeactivate() {
		doShutdown();
	}

	@Override
	public void doShutdown() {
		list.forEach(i -> i.unregister() );
		list.clear();
	}

	@Override
	public void doStart(int port) throws Exception {
		GrpcServerImpl service = new GrpcServerImpl();
		service.start(port);
		
		BundleContext ctx = FrameworkUtil.getBundle(DevGrpcAdminImpl.class).getBundleContext();
		
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put("port", port);
		ServiceRegistration<GrpcServer> reg = ctx.registerService(GrpcServer.class, service, properties);
		
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
