package org.ehcache.sample;

import static org.ehcache.config.builders.ResourcePoolsBuilder.heap;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.impl.config.copy.DefaultCopierConfiguration;
import org.ehcache.impl.copy.SerializingCopier;

public class OnHeapStoreByValueTest {

    public static void main(String[] args) {


        OnHeapStoreByValueTest main = new OnHeapStoreByValueTest();
        System.out.println(">>> testStoreByValue");
        main.testStoreByValue();
        System.out.println(">>> testComplexValues");
        main.testComplexValues();
        
    }

    
    /**
     * Use the cache to park values, no copy not serialization
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void testComplexValues() {

        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(false);
        cacheManager.init();
        
        DefaultCopierConfiguration<String> copierConfigurationKey = new DefaultCopierConfiguration(
                NoneCopier.class, DefaultCopierConfiguration.Type.KEY);
        DefaultCopierConfiguration<String> copierConfigurationValue = new DefaultCopierConfiguration(
                NoneCopier.class, DefaultCopierConfiguration.Type.VALUE);
        
        final Cache<String, TestContainer> cache = cacheManager.createCache("cache1", 
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, TestContainer.class, heap(2))
                .withService(copierConfigurationKey)
                .withService(copierConfigurationValue)
                .build());

        cache.put("a", new TestContainer("a"));
        cache.put("b", new TestContainer("b"));
        cache.put("c", new TestContainer("c"));
        
        System.out.println(cache.get("a"));
        System.out.println(cache.get("b"));
        System.out.println(cache.get("c"));
        
    }


    @SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
    public void testStoreByValue() {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(false);
        cacheManager.init();
        DefaultCopierConfiguration<String> copierConfiguration = new DefaultCopierConfiguration(SerializingCopier.class, DefaultCopierConfiguration.Type.VALUE);
        final Cache<Long, String> cache1 = cacheManager.createCache("cache1", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, heap(1)).build());
        performTest(cache1, true);
        final Cache<Long, String> cache2 = cacheManager.createCache("cache2", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, heap(1)).add(copierConfiguration).build());
        performTest(cache2, false);
        final Cache<Long, String> cache3 = cacheManager.createCache("cache3", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, heap(1)).build());
        performTest(cache3, true);
        cacheManager.close();
    }


    private void performTest(Cache<Long, String> cache, boolean same) {
        
        cache.put(1L, "one");
        String s1 = cache.get(1L);
        String s2 = cache.get(1L);
        String s3 = cache.get(1L);
     
        if ((s1 == s2) != same)
            System.out.println("s1, s2: Failed");
        if ((s2 == s3) != same)
            System.out.println("s2, s3: Failed");
        
    }
    
}
