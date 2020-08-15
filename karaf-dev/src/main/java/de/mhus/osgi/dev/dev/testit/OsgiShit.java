package de.mhus.osgi.dev.dev.testit;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import de.mhus.lib.core.MSystem;
import de.mhus.osgi.dev.dev.CmdDev;

public class OsgiShit implements ShitIfc {

    private static ServiceRegistration<EventHandler> registrationEventHandler;
    public static String[] blacklist =
            new String[] {
                "org/osgi/service/log/LogEntry" // ignore ... too much useless events
            };

    @Override
    public void printUsage() {
        System.out.println("sessionid                      - print current session id");
        System.out.println(
                "registerEventHandler <topic>*  - register/unregister event handler, use *, e.g. com/acme/reportgenerator/*");
        System.out.println(
                "blacklist [starts with]*       - set event handler blacklist, e.g. org/osgi/service/log/LogEntry");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object doExecute(CmdShitYo base, String cmd, String[] parameters) throws Exception {

        if (cmd.equals("blacklist")) {
            blacklist = parameters;
        } else if (cmd.equals("registerEventHandler")) {
            if (registrationEventHandler == null) {
                @SuppressWarnings("rawtypes")
                Dictionary props = new Hashtable();
                props.put(EventConstants.EVENT_TOPIC, parameters);
                BundleContext bundleContext =
                        FrameworkUtil.getBundle(CmdDev.class).getBundleContext();
                System.out.println("Register");
                registrationEventHandler =
                        bundleContext.registerService(
                                EventHandler.class.getName(), new PrintEventHandler(), props);
            } else {
                System.out.println("Unregister");
                registrationEventHandler.unregister();
                registrationEventHandler = null;
            }
        } else if (cmd.equals("sessionid")) {
            System.out.println(MSystem.getObjectId(base.getSession()));
        }

        return null;
    }
}
