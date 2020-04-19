package org.ehcache.sample;

public class TestContainer {

    private String name;

    public TestContainer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
