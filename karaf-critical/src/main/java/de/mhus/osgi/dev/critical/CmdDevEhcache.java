package de.mhus.osgi.dev.critical;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import com.hazelcast.cache.HazelcastCachingProvider;
import com.hazelcast.cache.impl.HazelcastServerCachingProvider;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import de.mhus.osgi.api.karaf.AbstractCmd;

@Command(scope = "mhus", name = "dev-ehcache", description = "Ehcache tests")
@Service
public class CmdDevEhcache extends AbstractCmd {

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
    
    static HazelcastServerCachingProvider provider = null;
    static CacheManager cacheManager = null;
    
    @Override
    public Object execute2() throws Exception {


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
