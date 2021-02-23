/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.osgi.dev.jpa.impl;

import java.util.List;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import de.mhus.db.osgi.api.adb.AbstractCommonAdbConsumer;
import de.mhus.db.osgi.api.adb.CommonDbConsumer;
import de.mhus.db.osgi.api.adb.ReferenceCollector;
import de.mhus.lib.errors.MException;
import de.mhus.lib.xdb.XdbService;

@Component(
        service = CommonDbConsumer.class,
        property = "commonService=common_adb",
        immediate = true)
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
    public void collectReferences(Object object, ReferenceCollector collector, String reason) {}

    @Override
    public void doCleanup() {}

    @Override
    public void doPostInitialize(XdbService manager) throws Exception {}

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
