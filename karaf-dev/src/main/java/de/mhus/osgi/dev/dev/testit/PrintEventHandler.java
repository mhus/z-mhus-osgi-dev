package de.mhus.osgi.dev.dev.testit;

import java.util.Arrays;
import java.util.TreeMap;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class PrintEventHandler implements EventHandler {

    @Override
    public void handleEvent(Event event) {

        if (OsgiShit.blacklist != null)
            for (String black : OsgiShit.blacklist) if (event.getTopic().startsWith(black)) return;

        TreeMap<String, Object> m = new TreeMap<>();
        for (String name : event.getPropertyNames()) {
            Object v = event.getProperty(name);
            if (v == null) // paranoia
            v = "null";
            if (v.getClass().isArray()) v = Arrays.deepToString((Object[]) v);
            m.put(name, v);
        }
        System.out.println("EVENT: " + event.getTopic() + " " + m);
    }
}
