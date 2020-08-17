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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import de.mhus.db.osgi.api.adb.AdbOsgiUtil;
import de.mhus.db.osgi.api.adb.AdbService;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.errors.MException;

public class AdbTest01 {

    public static void test() throws MalformedURLException, MException {
        AdbService service = AdbOsgiUtil.getCommonAdbService();
        DbManager db = service.getManager();

        AdbPageEntry entry = db.inject(new AdbPageEntry());
        entry.setLinkDestination(new URL("http://www.google.com"));
        entry.setLinkName("Entry " + new Date());
        entry.save();

        for (AdbPageEntry entry2 : db.getAll(AdbPageEntry.class))
            System.out.println(entry2.getId() + " " + entry2.getLinkName());
    }
}
