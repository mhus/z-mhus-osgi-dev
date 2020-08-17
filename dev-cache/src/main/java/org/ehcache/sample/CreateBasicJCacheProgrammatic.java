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
package org.ehcache.sample;

import org.ehcache.config.Configuration;
import org.ehcache.config.FluentConfigurationBuilder;
import org.slf4j.Logger;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/** Created by fabien.sanglier on 10/6/16. */
public class CreateBasicJCacheProgrammatic extends BaseJCacheTester {
    private static final Logger LOGGER = getLogger(CreateBasicJCacheProgrammatic.class);

    private static String CACHE_NAME = "myJCache";

    public static void main(String[] args) throws Exception {
        // pass in the number of object you want to generate, default is 100
        int numberOfObjects = Integer.parseInt(args.length == 0 ? "5000" : args[0]);
        int numberOfIteration = Integer.parseInt(args.length == 0 ? "5" : args[1]);
        int sleepTimeMillisBetweenIterations =
                Integer.parseInt(args.length == 0 ? "1000" : args[2]);

        new CreateBasicJCacheProgrammatic()
                .run(numberOfIteration, numberOfObjects, sleepTimeMillisBetweenIterations);

        LOGGER.info("Exiting");
    }

    public void run(
            int numberOfIteration,
            int numberOfObjectPerIteration,
            int sleepTimeMillisBetweenIterations)
            throws Exception {
        LOGGER.info(
                "JCache testing BEGIN - Creating JCache Programmatically without any XML config");

        // finds ehcache provider automatically if it is in the classpath
        // FrameworkUtil.getBundle(Ehcache.class).loadClass("");
        // ServiceLoader<CachingProvider> cl = ServiceLoader.load(CachingProvider.class,
        // org.ehcache.core.Ehcache.class.getClassLoader() );
        // Optional<CachingProvider> first = cl.findFirst();
        // CachingProvider cachingProvider = first.get();
        // if (cachingProvider == null)
        //     throw new NullPointerException("CachingProvider is null");
        // CachingProvider cachingProvider = Caching.getCachingProvider();

        //    CacheManagerBuilder<org.ehcache.CacheManager> config = newCacheManagerBuilder()
        //            .withCache("basicCache",
        //              newCacheConfigurationBuilder(Long.class, String.class, heap(100).offheap(1,
        // MB)))
        //            ;

        FluentConfigurationBuilder<?> configBuilder =
                org.ehcache.config.builders.ConfigurationBuilder.newConfigurationBuilder();
        Configuration config = configBuilder.build();

        @SuppressWarnings("resource")
        CachingProvider cachingProvider = new DummyCachingProvider(config);

        // If there are multiple providers in your classpath, use the fully qualified name to
        // retrieve the Ehcache caching provider.
        // CachingProvider cachingProvider =
        // Caching.getCachingProvider("de.mhus.osgi.dev.critical.ehcache.jsr107.EhcacheCachingProvider");

        try (CacheManager cacheManager = cachingProvider.getCacheManager()) {
            Cache<Long, String> myJCache =
                    cacheManager.createCache(
                            CACHE_NAME,
                            new MutableConfiguration<Long, String>()
                                    .setTypes(Long.class, String.class)
                                    .setStoreByValue(false)
                                    .setStatisticsEnabled(true)
                                    .setExpiryPolicyFactory(
                                            FactoryBuilder.factoryOf(
                                                    new CreatedExpiryPolicy(
                                                            new Duration(TimeUnit.SECONDS, 5)))));

            simpleGetsAndPutsCacheTest(
                    myJCache,
                    numberOfIteration,
                    numberOfObjectPerIteration,
                    sleepTimeMillisBetweenIterations,
                    new KeyValueGenerator<Long, String>() {
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
        LOGGER.info(
                "JCache testing DONE - Creating JCache Programmatically without any XML config");
    }
}
