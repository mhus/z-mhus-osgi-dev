package org.ehcache.sample;

import java.net.URI;
import java.util.Properties;

import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;

public class DummyCachingProvider implements CachingProvider {

    private org.ehcache.CacheManager ehCacheManager;

    public DummyCachingProvider(org.ehcache.CacheManager ehCacheManager) {
        this.ehCacheManager = ehCacheManager;
        
    }

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ClassLoader getDefaultClassLoader() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URI getDefaultURI() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Properties getDefaultProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CacheManager getCacheManager() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void close() {
        ehCacheManager.close();

    }

    @Override
    public void close(ClassLoader classLoader) {
        ehCacheManager.close();

    }

    @Override
    public void close(URI uri, ClassLoader classLoader) {
        ehCacheManager.close();
    }

    @Override
    public boolean isSupported(OptionalFeature optionalFeature) {
        // TODO Auto-generated method stub
        return false;
    }

}
