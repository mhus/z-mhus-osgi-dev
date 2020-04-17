package de.mhus.osgi.dev.dev.osgi;

import org.osgi.service.component.annotations.Component;

@Component(service = ITestService.class, immediate = true)
public class TestService extends AbstractTestService {

    @Override
    public String saySo() {
        return "so ... " + configurationAdmin;
    }

}
