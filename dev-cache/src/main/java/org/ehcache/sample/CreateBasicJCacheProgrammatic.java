package org.ehcache.sample;

import org.ehcache.config.Configuration;
import org.ehcache.config.FluentConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.core.Ehcache;
import org.ehcache.core.EhcacheManager;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.core.spi.ServiceLocator;
import org.ehcache.core.spi.service.ServiceUtils;
import org.ehcache.impl.config.serializer.DefaultSerializationProviderConfiguration;
import org.ehcache.impl.serialization.PlainJavaSerializer;
import de.mhus.osgi.dev.critical.ehcache.jsr107.EhcacheCachingProvider;
import de.mhus.osgi.dev.critical.ehcache.jsr107.Jsr107Service;
import de.mhus.osgi.dev.critical.ehcache.jsr107.config.Jsr107Configuration;
import de.mhus.osgi.dev.critical.ehcache.jsr107.internal.DefaultJsr107Service;
import de.mhus.osgi.dev.critical.ehcache.jsr107.internal.tck.Eh107MBeanServerBuilder;
import org.ehcache.spi.service.ServiceCreationConfiguration;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;
import static org.ehcache.config.builders.ResourcePoolsBuilder.heap;
import static org.ehcache.config.units.MemoryUnit.MB;
import static org.slf4j.LoggerFactory.getLogger;
import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;

/**
 * Created by fabien.sanglier on 10/6/16.
 */
public class CreateBasicJCacheProgrammatic extends BaseJCacheTester {
  private static final Logger LOGGER = getLogger(CreateBasicJCacheProgrammatic.class);

  private static String CACHE_NAME = "myJCache";

  public static void main(String[] args) throws Exception {
    // pass in the number of object you want to generate, default is 100
    int numberOfObjects = Integer.parseInt(args.length == 0 ? "5000" : args[0]);
    int numberOfIteration = Integer.parseInt(args.length == 0 ? "5" : args[1]);
    int sleepTimeMillisBetweenIterations = Integer.parseInt(args.length == 0 ? "1000" : args[2]);

    new CreateBasicJCacheProgrammatic().run(numberOfIteration, numberOfObjects, sleepTimeMillisBetweenIterations);

    LOGGER.info("Exiting");
  }


  public void run(int numberOfIteration, int numberOfObjectPerIteration, int sleepTimeMillisBetweenIterations) throws Exception {
    LOGGER.info("JCache testing BEGIN - Creating JCache Programmatically without any XML config");

    //finds ehcache provider automatically if it is in the classpath
    // FrameworkUtil.getBundle(Ehcache.class).loadClass("");
    // ServiceLoader<CachingProvider> cl = ServiceLoader.load(CachingProvider.class, org.ehcache.core.Ehcache.class.getClassLoader() );
    // Optional<CachingProvider> first = cl.findFirst();
    // CachingProvider cachingProvider = first.get();
    // if (cachingProvider == null)
    //     throw new NullPointerException("CachingProvider is null");
    // CachingProvider cachingProvider = Caching.getCachingProvider();
    
//    CacheManagerBuilder<org.ehcache.CacheManager> config = newCacheManagerBuilder()
//            .withCache("basicCache",
//              newCacheConfigurationBuilder(Long.class, String.class, heap(100).offheap(1, MB)))
//            ;
   
    FluentConfigurationBuilder<?> configBuilder = org.ehcache.config.builders.ConfigurationBuilder.newConfigurationBuilder();
    Configuration config = configBuilder.build();
    
    @SuppressWarnings("resource")
    CachingProvider cachingProvider = new DummyCachingProvider(config);

    // If there are multiple providers in your classpath, use the fully qualified name to retrieve the Ehcache caching provider.
    //CachingProvider cachingProvider = Caching.getCachingProvider("de.mhus.osgi.dev.critical.ehcache.jsr107.EhcacheCachingProvider");

    try (CacheManager cacheManager = cachingProvider.getCacheManager()) {
      Cache<Long, String> myJCache = cacheManager.createCache(
        CACHE_NAME,
        new MutableConfiguration<Long, String>()
          .setTypes(Long.class, String.class)
          .setStoreByValue(false)
          .setStatisticsEnabled(true)
          .setExpiryPolicyFactory(FactoryBuilder.factoryOf(new CreatedExpiryPolicy(new Duration(TimeUnit.SECONDS, 5)))));

      simpleGetsAndPutsCacheTest(myJCache, numberOfIteration, numberOfObjectPerIteration, sleepTimeMillisBetweenIterations, new KeyValueGenerator<Long, String>() {
        @Override
        public Long getKey(Number k) {
          return new Long(k.longValue());
        }

        @Override
        public String getValue(Number v) {
          return String.format("Da One %s!!", v.toString());
        }
      });
    }
    LOGGER.info("JCache testing DONE - Creating JCache Programmatically without any XML config");
  }
}