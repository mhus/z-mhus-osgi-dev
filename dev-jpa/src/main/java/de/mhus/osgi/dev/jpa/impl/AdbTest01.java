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
        entry.setLinkDestination(new URL( "http://www.google.com" ));
        entry.setLinkName( "Entry " + new Date());
        entry.save();
        
        for (AdbPageEntry entry2 : db.getAll(AdbPageEntry.class))
            System.out.println(entry2.getId() + " " + entry2.getLinkName());
        
    }

}
