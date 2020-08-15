package de.mhus.osgi.dev.cache;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.RedissonRxClient;
import org.redisson.config.Config;

import de.mhus.osgi.api.karaf.AbstractCmd;

@Command(scope = "mhus", name = "dev-redis", description = "Redis tests")
@Service
public class CmdDevRedis extends AbstractCmd {

    @Argument(
            index = 0,
            name = "cmd",
            required = true,
            description = "Command to execute\n" + "",
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

        Config config = new Config();
        config.useSingleServer().setAddress("redis://redisserver:6379");
        // .useClusterServers()
        // use "rediss://" for SSL connection
        //    	      .addNodeAddress("redis://redisserver:6379");

        RedissonClient redisson = Redisson.create(config);

        RedissonReactiveClient redissonReactive = Redisson.createReactive(config);

        RedissonRxClient redissonRx = Redisson.createRx(config);

        redisson.shutdown();
        return null;
    }
}
