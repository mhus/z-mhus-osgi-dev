package de.mhus.osgi.dev.cache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import com.hazelcast.cache.HazelcastCachingProvider;
import com.hazelcast.cache.impl.HazelcastServerCachingProvider;
import com.hazelcast.cluster.Member;
import com.hazelcast.config.Config;
import com.hazelcast.core.DistributedObjectEvent;
import com.hazelcast.core.DistributedObjectListener;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.osgi.HazelcastOSGiInstance;
import com.hazelcast.osgi.HazelcastOSGiService;

import de.mhus.lib.core.M;
import de.mhus.osgi.api.karaf.AbstractCmd;

@Command(scope = "mhus", name = "dev-hazelcast", description = "Hazelcast tests")
@Service
public class CmdDevHazelcast extends AbstractCmd {

    @Argument(
            index = 0,
            name = "cmd",
            required = true,
            description =
                    "Command to execute"
            ,
            multiValued = false)
    String cmd;

    @Argument(
            index = 1,
            name = "paramteters",
            required = false,
            description = "Parameters",
            multiValued = true)
    String[] parameters;
    
    HazelcastServerCachingProvider provider = null;
    CacheManager cacheManager = null;
    
    @Override
    public Object execute2() throws Exception {

        
        HazelcastOSGiService service = M.l(HazelcastOSGiService.class);
        HazelcastOSGiInstance inst = service.getHazelcastInstanceByName("mhus");
        if (inst == null) {
            System.out.println("Create");
            Config config = new Config("mhus");
            inst = service.newHazelcastInstance(config);
        }
        System.out.println(inst);
        
        if (cmd.equals("node-list")) {
            for (Member member : inst.getCluster().getMembers()) {
                System.out.println("Node: " + member);
            }
        }
        
//        HazelcastInstance instance = Hazelcast.getHazelcastInstanceByName("cellar");

        HazelcastInstance instance = null;
        
//        ClassLoader appClassLoader = getClass().getClassLoader();
//        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
//        try {
//            Thread.currentThread().setContextClassLoader(appClassLoader);
//            // HazelcastInstance instance = Hazelcast.newHazelcastInstance();
//            instance = Hazelcast.getHazelcastInstanceByName("cellar");
//            //CachingProvider cp = HazelcastServerCachingProvider.createCachingProvider(instance);
//            //cacheManager = cp.getCacheManager(cp.getDefaultURI(), appClassLoader);
//        } finally {
//            Thread.currentThread().setContextClassLoader(tccl);
//        }
//
        if (cmd.startsWith("cache")) {
            if (provider == null) {
                provider = new HazelcastServerCachingProvider(instance);
//                cacheManager = provider.getCacheManager();
            }
            ClassLoader appClassLoader = getClass().getClassLoader();
            ClassLoader tccl = Thread.currentThread().getContextClassLoader();
            try {
              Thread.currentThread().setContextClassLoader(appClassLoader);
              CachingProvider cachingProvider = Caching.getCachingProvider(HazelcastServerCachingProvider.class.getName());
              cacheManager = cachingProvider.getCacheManager();
            } finally {
              Thread.currentThread().setContextClassLoader(tccl);
            }
        }
//            HazelcastServerCachingProvider provider = (HazelcastServerCachingProvider) Caching.getCachingProvider(HazelcastServerCachingProvider.class.getName()); // or add class name of Hazelcast server caching provider to disambiguate
//        if (cacheManager == null)
//           cacheManager = provider.getCacheManager(null, null, HazelcastCachingProvider.propertiesByInstanceItself(instance));
//        }            
//            CachingProvider cachingProvider = Caching.getCachingProvider(HazelcastServerCachingProvider.class.getName());
//            CacheManager cacheManager = cachingProvider.getCacheManager();
            System.out.println(cacheManager);
        
//        HazelcastOSGiService service = M.l(HazelcastOSGiService.class);
//        HazelcastOSGiInstance instance = service.newHazelcastInstance();
//
//        CachingProvider cp = HazelcastServerCachingProvider.createCachingProvider(instance);
//        CacheManager cm = cp.getCacheManager(cp.getDefaultURI(), getClass().getClassLoader());
//        System.out.println(cm);
        
//        
//        CachingProvider cachingProvider = Caching.getCachingProvider(HazelcastServerCachingProvider.class.getName());
//        
//        CacheManager cacheManager = cachingProvider.getCacheManager(null, null,
//                propertiesByInstanceItself(instance));

        
//        CacheManager cacheManager = null;
//        CachingProvider cachingProvider = null;

//        for (HazelcastInstance inst : Hazelcast.getAllHazelcastInstances()) {
//            System.out.println(inst.getName());
//        }
//
//        HazelcastInstance inst = Hazelcast.getHazelcastInstanceByName("cellar");
//        ICacheManager ma = inst.getCacheManager();
        
        
            
            if (cmd.equals("caches")) {
                System.out.println("Caches:");
                for (String name : cacheManager.getCacheNames())
                    System.out.println(name);
            } else if (cmd.equals("cache.create")) {
                
                MutableConfiguration<String, String> config =
                        new MutableConfiguration<String, String>()
                            .setTypes( String.class, String.class );            
    
                
                Cache<String, String> cache = cacheManager.createCache("default", config);
                System.out.println(cache);
                
            } else if (cmd.equals("cache")) {
                Cache<String, String> cache = cacheManager.getCache( "default");
                System.out.println(cache);
            } else if (cmd.equals("cache.put")) {
                Cache<String, String> cache = cacheManager.getCache( "default");
                cache.put(parameters[0], parameters[1]);
                System.out.println("OK");
            } else if (cmd.equals("cache.get")) {
                Cache<String, String> cache = cacheManager.getCache( "default");
                String value = cache.get(parameters[0]);
                System.out.println(value);
            }

        return null;
    }

//    protected <K, V, C extends Configuration<K, V>> CacheConfig<K, V> createCacheConfig(String cacheName,
//            C configuration) {
//        CacheConfig<K, V> cacheConfig;
//        if (configuration instanceof CompleteConfiguration) {
//        cacheConfig = new CacheConfig<K, V>((CompleteConfiguration) configuration);
//        } else {
//        cacheConfig = new CacheConfig<K, V>();
//        cacheConfig.setStoreByValue(configuration.isStoreByValue());
//        final Class<K> keyType = configuration.getKeyType();
//        final Class<V> valueType = configuration.getValueType();
//        cacheConfig.setTypes(keyType, valueType);
//        }
//        cacheConfig.setName(cacheName);
//        cacheConfig.setManagerPrefix(cacheNamePrefix);
//        cacheConfig.setUriString(getURI().toString());
//        return cacheConfig;
//    }
    
    
}
