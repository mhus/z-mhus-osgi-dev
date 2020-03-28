package de.mhus.osgi.dev.jpa.impl;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import de.mhus.lib.core.MLog;

@Component(immediate=true)
public class JpaService extends MLog implements JpaApi {

	@Activate
	public void doActivate(BundleContext ctx) {
		
		
		
	}
	
}
