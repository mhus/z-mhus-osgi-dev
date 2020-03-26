package de.mhus.osgi.dev.critical;

import static java.net.URI.create;
import static org.ehcache.clustered.client.config.builders.ClusteredResourcePoolBuilder.clusteredDedicated;
import static org.ehcache.clustered.client.config.builders.ClusteringServiceConfigurationBuilder.cluster;
import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;
import static org.ehcache.config.builders.ResourcePoolsBuilder.heap;
import static org.ehcache.config.units.MemoryUnit.MB;

import java.net.URI;
import java.util.ServiceLoader;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.core.Ehcache;
import org.ehcache.sample.CreateBasicJCacheProgrammatic;

import de.mhus.lib.core.MCast;
import de.mhus.osgi.api.karaf.AbstractCmd;

@Command(scope = "mhus", name = "dev-ehcache", description = "Ehcache tests")
@Service
public class CmdDevEhcache extends AbstractCmd {

    @Argument(
            index = 0,
            name = "cmd",
            required = true,
            description =
                    "Command to execute\n"
                    + "  basic\n"
                    + "  clustered terracotta://tcserver:9410/clustered offheap-1 [key long] [value string] - set\n"
                    + "  clustered terracotta://tcserver:9410/clustered offheap-1 [key long] - get\n"
                    + ""
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
    
    @Override
    public Object execute2() throws Exception {


        if (cmd.equals("jsr107.base")) {
            if (parameters == null) parameters = new String[0];
            
            CreateBasicJCacheProgrammatic.main(parameters);

            return null;
        }

        if (cmd.equals("clustered")) {
            log().i("Creating clustered cache manager");
            // terracotta://tcserver:9410/clustered
            // default-resource
            URI uri = create(parameters[0]);
            try (CacheManager cacheManager = newCacheManagerBuilder()
                    .with(cluster(uri).autoCreate().defaultServerResource(parameters[1]))
                    .withCache("basicCache",
                            newCacheConfigurationBuilder(Long.class, String.class,
                                    heap(100).offheap(1, MB).with(clusteredDedicated(5, MB))))
                    .build(true)) {
              Cache<Long, String> basicCache = cacheManager.getCache("basicCache", Long.class, String.class);

              log().i("Putting to cache");
              basicCache.put(1L, "da one!");

              System.out.println("1: " + basicCache.get(1L) );
              if (parameters.length == 3) {
                  long k = MCast.tolong(parameters[2], 0);
                  System.out.println( k + ": " + basicCache.get(k) );
              } else
              if (parameters.length == 4) {
                  long k = MCast.tolong(parameters[2], 0);
                  String v = parameters[3];
                  System.out.println("Set " + k + " to " + v);
                  basicCache.put(k, v);
              }
              
              log().i("Closing cache manager");
            }

            log().i("Exiting");        }
        
        
        if (cmd.equals("basic")) {
            
            log().i("Creating cache manager programmatically");
            try (CacheManager cacheManager = newCacheManagerBuilder()
              .withCache("basicCache",
                newCacheConfigurationBuilder(Long.class, String.class, heap(100).offheap(1, MB)))
              .build(true)) {
              Cache<Long, String> basicCache = cacheManager.getCache("basicCache", Long.class, String.class);

              log().i("Putting to cache");
              basicCache.put(1L, "da one!");
              String value = basicCache.get(1L);
              log().i("Retrieved '{}'", value);

              log().i("Closing cache manager");
            }

            log().i("Exiting");
            
        }

        return null;
    }
    

    
}
