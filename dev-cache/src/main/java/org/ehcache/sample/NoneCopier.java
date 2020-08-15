package org.ehcache.sample;

import org.ehcache.impl.copy.ReadWriteCopier;

public class NoneCopier<T> extends ReadWriteCopier<T> {

    @Override
    public T copy(T obj) {
        return obj;
    }
}
