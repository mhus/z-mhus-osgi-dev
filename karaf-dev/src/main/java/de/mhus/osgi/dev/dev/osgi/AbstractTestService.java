package de.mhus.osgi.dev.dev.osgi;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

public abstract class AbstractTestService implements ITestService {


    protected ConfigurationAdmin configurationAdmin;

    @Activate
    public void doActivate(ComponentContext ctx) {
        System.out.println("AbstractTestService:doActivate");
    }

    @Modified
    public void doModify(ComponentContext ctx) {
        System.out.println("AbstractTestService:doModify");
    }

    @Deactivate
    public void doDeactivate(ComponentContext ctx) {
        System.out.println("AbstractTestService:doDeactivate");
    }

    @Reference
    public void setConfigurationAdmin(ConfigurationAdmin admin) {
        System.out.println("AbstractTestService:setConfigurationAdmin");
        configurationAdmin = admin;
    }

}
