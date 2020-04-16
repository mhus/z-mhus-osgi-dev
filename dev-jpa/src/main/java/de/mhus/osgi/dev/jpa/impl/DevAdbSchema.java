package de.mhus.osgi.dev.jpa.impl;

import java.util.List;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import de.mhus.db.osgi.api.adb.AbstractCommonAdbConsumer;
import de.mhus.db.osgi.api.adb.CommonAdbConsumer;
import de.mhus.db.osgi.api.adb.ReferenceCollector;
import de.mhus.lib.errors.MException;
import de.mhus.lib.xdb.XdbService;

@Component(service = CommonAdbConsumer.class,immediate = true)
public class DevAdbSchema extends AbstractCommonAdbConsumer {

    @Activate
    public void doActivate(ComponentContext ctx) {
        System.out.println("DevAdbSchema: Start AdbDbSchema");
    }
    
    @Override
    public void registerObjectTypes(List<Class<?>> list) {
        System.out.println("DevAdbSchema: AdbDbSchema::registerObjectTypes");
        list.add(AdbPageEntry.class);
    }

    @Override
    public void doInitialize() {
        System.out.println("DevAdbSchema: AdbDbSchema::doInitialize");
    }

    @Override
    public void doDestroy() {
        System.out.println("DevAdbSchema: AdbDbSchema::doDestroy");
    }

    @Override
    public void collectReferences(Object object, ReferenceCollector collector) {
        
    }

    @Override
    public void doCleanup() {
        
    }

    @Override
    public void doPostInitialize(XdbService manager) throws Exception {
        
    }

    @Override
    public boolean canCreate(Object obj) throws MException {
        return true;
    }
    
    @Override
    public boolean canRead(Object obj) throws MException {
        return true;
    }

    @Override
    public boolean canUpdate(Object obj) throws MException {
        return true;
    }

    @Override
    public boolean canDelete(Object obj) throws MException {
        return true;
    }
    
    
}
