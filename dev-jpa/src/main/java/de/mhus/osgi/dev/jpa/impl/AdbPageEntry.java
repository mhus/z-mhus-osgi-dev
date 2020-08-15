package de.mhus.osgi.dev.jpa.impl;

import java.net.URL;
import java.util.UUID;

import de.mhus.lib.adb.DbComfortableObject;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;

public class AdbPageEntry extends DbComfortableObject {

    @DbPrimaryKey private UUID id;
    @DbPersistent private String linkName;
    @DbPersistent private URL linkDestination;

    public UUID getId() {
        return id;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public URL getLinkDestination() {
        return linkDestination;
    }

    public void setLinkDestination(URL linkDestination) {
        this.linkDestination = linkDestination;
    }
}
