package de.mhus.osgi.dev.dev.osgi;

import org.osgi.service.component.annotations.Component;

import de.mhus.lib.core.MSystem;

@Component(service = ITestService.class, immediate = true)
public class TestService extends AbstractTestService {

    @Override
    public String saySo() {
        return "so ... " + configurationAdmin + " " + MSystem.getObjectId(this);
    }
}
