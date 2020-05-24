package de.mhus.osgi.dev.cache;

import java.util.Date;
import java.util.Map.Entry;

import javax.cache.CacheManager;

import org.apache.karaf.cellar.core.ClusterManager;
import org.apache.karaf.cellar.core.GroupManager;
import org.apache.karaf.cellar.hazelcast.HazelcastClusterManager;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.osgi.service.cm.ConfigurationAdmin;

import com.hazelcast.cache.ICache;
import com.hazelcast.config.CacheSimpleConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICacheManager;
import com.hazelcast.core.ILock;
import com.hazelcast.core.Member;

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
                    "Command to execute\n"
                    + " hazelcast-list-instances"
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
    
    CacheManager cacheManager = null;
    
    @Override
    public Object execute2() throws Exception {

        
        ClusterManager clusterManager = M.l(ClusterManager.class);
        @SuppressWarnings("unused")
		GroupManager groupManager = M.l(GroupManager.class);
        @SuppressWarnings("unused")
		ConfigurationAdmin configurationAdmin = M.l(ConfigurationAdmin.class);
        
        if (cmd.equals("clustermanager")) {
        	System.out.println(clusterManager);
        	System.out.println(clusterManager.getClass());
        	HazelcastClusterManager hccm = (HazelcastClusterManager)clusterManager;
        	HazelcastInstance inst = hccm.getInstance();
        	System.out.println(inst);
        	for (Member member : inst.getCluster().getMembers()) {
                System.out.println("Node: " + member);
            }
        	ICacheManager cm = inst.getCacheManager();
        	System.out.println(cm);
        	System.out.println(cm.getClass());
        	for (Entry<String, CacheSimpleConfig> cc : inst.getConfig().getCacheConfigs().entrySet()) {
        		System.out.println("CacheConfig: " + cc.getKey() + "=" + cc.getValue());
        	}
        }
        if (cmd.equals("cache-init")) {
        	HazelcastClusterManager hccm = (HazelcastClusterManager)clusterManager;
        	HazelcastInstance inst = hccm.getInstance();
        	ICacheManager cm = inst.getCacheManager();
        	CacheSimpleConfig cc = inst.getConfig().getCacheConfig("test");
        	if (cc == null) {
        		cc = new CacheSimpleConfig();
        		cc.setName("test");
        		cc.setKeyType("java.lang.String");
        		cc.setValueType("java.lang.String");
        		inst.getConfig().addCacheConfig(cc);
        	}
        	ICache<String, String> testc = cm.getCache("test");
        	System.out.println(testc);
        }
        if (cmd.equals("cache-test")) {
        	HazelcastClusterManager hccm = (HazelcastClusterManager)clusterManager;
        	HazelcastInstance inst = hccm.getInstance();
        	ICacheManager cm = inst.getCacheManager();
        	ICache<String, String> testc = cm.getCache("test");
        	if (testc == null) {
        		System.out.println("Cache config not found");
        		return null;
        	}
        	String a = testc.get("a");
        	System.out.println("A: " + a);
        	String val = new Date().toString();
        	testc.put("a",val);
        	System.out.println("New Value: " + val);
        }
        if (cmd.equals("cache-set")) {
        	HazelcastClusterManager hccm = (HazelcastClusterManager)clusterManager;
        	HazelcastInstance inst = hccm.getInstance();
        	ICacheManager cm = inst.getCacheManager();
        	ICache<String, String> testc = cm.getCache("test");
        	if (testc == null) {
        		System.out.println("Cache config not found");
        		return null;
        	}
        	testc.put(parameters[0], parameters[1]);
        	System.out.println("SET");
        }
        if (cmd.equals("cache-get")) {
        	HazelcastClusterManager hccm = (HazelcastClusterManager)clusterManager;
        	HazelcastInstance inst = hccm.getInstance();
        	ICacheManager cm = inst.getCacheManager();
        	ICache<String, String> testc = cm.getCache("test");
        	if (testc == null) {
        		System.out.println("Cache config not found");
        		return null;
        	}
        	String val = testc.get(parameters[0]);
        	System.out.println("Value: " + val);
        }
        if (cmd.equals("lock")) {
        	String lockName = parameters == null || parameters.length == 0 ? "testlock" : parameters[0];
        	HazelcastClusterManager hccm = (HazelcastClusterManager)clusterManager;
        	HazelcastInstance inst = hccm.getInstance();
        	ILock lock = inst.getLock(lockName);
        	boolean res = lock.tryLock();
        	System.out.println("Get Lock " + lockName + ": "+ res);
        }
        if (cmd.equals("unlock")) {
        	String lockName = parameters == null || parameters.length == 0 ? "testlock" : parameters[0];
        	HazelcastClusterManager hccm = (HazelcastClusterManager)clusterManager;
        	HazelcastInstance inst = hccm.getInstance();
        	ILock lock = inst.getLock(lockName);
        	lock.unlock();
        	System.out.println("unlocked " + lockName);
        }
        if (cmd.equals("forceunlock")) {
        	String lockName = parameters == null || parameters.length == 0 ? "testlock" : parameters[0];
        	HazelcastClusterManager hccm = (HazelcastClusterManager)clusterManager;
        	HazelcastInstance inst = hccm.getInstance();
        	ILock lock = inst.getLock(lockName);
        	lock.forceUnlock();
        	System.out.println("unlocked " + lockName);
        }
        
        
/*        
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
*/
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
