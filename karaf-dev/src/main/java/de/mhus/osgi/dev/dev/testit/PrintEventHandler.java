package de.mhus.osgi.dev.dev.testit;

import java.util.TreeMap;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class PrintEventHandler implements EventHandler{

    @Override
    public void handleEvent(Event event) {
        TreeMap<String,Object> m = new TreeMap<>();
        for (String name: event.getPropertyNames())
            m.put(name, event.getProperty(name));
        System.out.println("EVENT: " + event.getTopic() + " " + m);
    }

}
