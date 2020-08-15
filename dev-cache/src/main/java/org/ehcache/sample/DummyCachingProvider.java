package org.ehcache.sample;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Properties;
import java.util.function.UnaryOperator;

import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;

import org.ehcache.config.Configuration;
import org.ehcache.core.EhcacheManager;
import org.ehcache.core.spi.ServiceLocator;
import org.ehcache.core.spi.service.ServiceUtils;
import org.ehcache.impl.config.serializer.DefaultSerializationProviderConfiguration;
import org.ehcache.impl.serialization.PlainJavaSerializer;
import org.ehcache.spi.service.ServiceCreationConfiguration;

import de.mhus.osgi.dev.cache.ehcache.jsr107.ConfigurationMerger;
import de.mhus.osgi.dev.cache.ehcache.jsr107.Eh107CacheLoaderWriterProvider;
import de.mhus.osgi.dev.cache.ehcache.jsr107.Eh107CacheManager;
import de.mhus.osgi.dev.cache.ehcache.jsr107.EhcacheCachingProvider;
import de.mhus.osgi.dev.cache.ehcache.jsr107.Jsr107Service;
import de.mhus.osgi.dev.cache.ehcache.jsr107.config.Jsr107Configuration;
import de.mhus.osgi.dev.cache.ehcache.jsr107.internal.DefaultJsr107Service;

public class DummyCachingProvider extends EhcacheCachingProvider {

    private static final String DEFAULT_URI_STRING = "urn:X-ehcache:jsr107-default-config";

    private Eh107CacheManager cacheManager;
    private URI uri;
    private Properties properties;

    public DummyCachingProvider(Configuration config) throws URISyntaxException {
        // from EhcacheCachingProvider
        uri = new URI(DEFAULT_URI_STRING);

        this.properties = new Properties();
        this.cacheManager = createCacheManager(uri, config, properties);
    }

    private Eh107CacheManager createCacheManager(
            URI uri, Configuration config, Properties properties) {
        Collection<ServiceCreationConfiguration<?, ?>> serviceCreationConfigurations =
                config.getServiceCreationConfigurations();

        Jsr107Service jsr107Service =
                new DefaultJsr107Service(
                        ServiceUtils.findSingletonAmongst(
                                Jsr107Configuration.class, serviceCreationConfigurations));
        Eh107CacheLoaderWriterProvider cacheLoaderWriterFactory =
                new Eh107CacheLoaderWriterProvider();
        @SuppressWarnings({"unchecked", "rawtypes"})
        DefaultSerializationProviderConfiguration serializerConfiguration =
                new DefaultSerializationProviderConfiguration()
                        .addSerializerFor(Object.class, (Class) PlainJavaSerializer.class);

        UnaryOperator<ServiceLocator.DependencySet> customization =
                dependencies -> {
                    ServiceLocator.DependencySet d =
                            dependencies.with(jsr107Service).with(cacheLoaderWriterFactory);
                    if (ServiceUtils.findSingletonAmongst(
                                    DefaultSerializationProviderConfiguration.class,
                                    serviceCreationConfigurations)
                            == null) {
                        d = d.with(serializerConfiguration);
                    }
                    return d;
                };

        org.ehcache.CacheManager ehcacheManager =
                new EhcacheManager(config, customization, !jsr107Service.jsr107CompliantAtomics());
        ehcacheManager.init();

        return new Eh107CacheManager(
                this,
                ehcacheManager,
                jsr107Service,
                properties,
                config.getClassLoader(),
                uri,
                new ConfigurationMerger(config, jsr107Service, cacheLoaderWriterFactory));
    }

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
        return cacheManager;
    }

    @Override
    public ClassLoader getDefaultClassLoader() {
        return cacheManager.getClassLoader();
    }

    @Override
    public URI getDefaultURI() {
        return uri;
    }

    @Override
    public Properties getDefaultProperties() {
        return properties;
    }

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
        return cacheManager;
    }

    @Override
    public CacheManager getCacheManager() {
        return cacheManager;
    }

    @Override
    public void close() {
        if (cacheManager != null) cacheManager.close();
        cacheManager = null;
    }

    @Override
    public void close(ClassLoader classLoader) {
        close();
    }

    @Override
    public void close(URI uri, ClassLoader classLoader) {
        close();
    }

    @Override
    public boolean isSupported(OptionalFeature optionalFeature) {
        return false;
    }
}
