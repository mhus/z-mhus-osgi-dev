package de.mhus.osgi.dev.dev.osgi;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventAdmin;

import de.mhus.lib.annotations.service.ServiceActivate;
import de.mhus.lib.annotations.service.ServiceComponent;
import de.mhus.lib.annotations.service.ServiceDeactivate;
import de.mhus.lib.annotations.service.ServiceReference;
import de.mhus.lib.core.MLog;
import de.mhus.osgi.api.services.ISimpleService;

// sb-create de.mhus.osgi.dev.dev.osgi.MhusTestService
@ServiceComponent(property = "test=test")
public class MhusTestService extends MLog implements ISimpleService {

//    @ServiceReference
//    public BundleContext bcontext;
    
    @ServiceActivate
    public void doActivate() {
        log().i("doActivate");
    }
    
    @ServiceDeactivate
    public void doDeactivate() {
        log().i("doDeactivate");
    }
    
    @ServiceReference
    public void setContext(BundleContext context) {
        log().i("Set Context",context);
    }
    
    @ServiceReference
    public void setEventAdmin(EventAdmin eventAdmin) {
        log().i("Set Event Admin",eventAdmin);
    }
    
    @Override
    public String getSimpleServiceInfo() {
        return "test";
    }

    @Override
    public String getSimpleServiceStatus() {
        return "hello";
    }

    @Override
    public void doSimpleServiceCommand(String cmd, Object... param) {
        
    }

}
