package de.mhus.osgi.dev.jpa.impl;

import java.util.List;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import de.mhus.lib.adb.Persistable;
import de.mhus.lib.errors.MException;
import de.mhus.lib.xdb.XdbService;
import de.mhus.osgi.api.adb.AbstractDbSchemaService;
import de.mhus.osgi.api.adb.DbSchemaService;
import de.mhus.osgi.api.adb.ReferenceCollector;

@Component(service = DbSchemaService.class,immediate = true)
public class DevAdbSchema extends AbstractDbSchemaService {

    @Activate
    public void doActivate(ComponentContext ctx) {
        System.out.println("Start AdbDbSchema");
    }
    
    @Override
    public void registerObjectTypes(List<Class<? extends Persistable>> list) {
        System.out.println("AdbDbSchema::registerObjectTypes");
        list.add(AdbPageEntry.class);
    }

    @Override
    public void doInitialize(XdbService dbService) {
        System.out.println("AdbDbSchema::doInitialize");
    }

    @Override
    public void doDestroy() {
        System.out.println("AdbDbSchema::doDestroy");
    }

    @Override
    public void collectReferences(Persistable object, ReferenceCollector collector) {
        
    }

    @Override
    public void doCleanup() {
        
    }

    @Override
    public void doPostInitialize(XdbService manager) throws Exception {
        
    }

    @Override
    public boolean canCreate(Persistable obj) throws MException {
        return true;
    }
    
    @Override
    public boolean canRead(Persistable obj) throws MException {
        return true;
    }

    @Override
    public boolean canUpdate(Persistable obj) throws MException {
        return true;
    }

    @Override
    public boolean canDelete(Persistable obj) throws MException {
        return true;
    }
    
    
}
