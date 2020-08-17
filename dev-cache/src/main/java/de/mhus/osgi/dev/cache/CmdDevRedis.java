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
