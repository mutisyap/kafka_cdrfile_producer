package io.pmutisya.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastConfiguration {
    private static final String instanceName = "kcp";
    private static HazelcastInstance hazelcastInstance;

    public static HazelcastInstance getHazelcastInstance() {
        if (hazelcastInstance != null) {
            return hazelcastInstance;
        }

        Config config = new Config();
        config.setInstanceName(instanceName)
                .addMapConfig(new MapConfig()
                        .setName("configuration"));

        return hazelcastInstance = Hazelcast.newHazelcastInstance(config);
    }
}
