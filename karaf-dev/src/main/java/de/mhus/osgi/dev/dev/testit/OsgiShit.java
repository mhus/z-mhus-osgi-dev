package de.mhus.osgi.dev.dev.testit;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import de.mhus.osgi.dev.dev.CmdDev;

public class OsgiShit implements ShitIfc {

    private static ServiceRegistration<EventHandler> registrationEventHandler;
    
    @Override
    public void printUsage() {
        System.out.println("registerEventHandler <topic>*  - register/unregister event handler, use *, e.g. com/acme/reportgenerator/*");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object doExecute(CmdShitYo base, String cmd, String[] parameters) throws Exception {

        if (cmd.equals("registerEventHandler")) {
            if (registrationEventHandler == null) {
                @SuppressWarnings("rawtypes")
                Dictionary props = new Hashtable();
                props.put(EventConstants.EVENT_TOPIC, parameters);
                BundleContext bundleContext = FrameworkUtil.getBundle(CmdDev.class).getBundleContext();
                System.out.println("Register");
                registrationEventHandler = bundleContext.registerService(EventHandler.class.getName(), new PrintEventHandler() , props);
            } else {
                System.out.println("Unregister");
                registrationEventHandler.unregister();
                registrationEventHandler = null;
            }
        }
            
        return null;
    }

}
