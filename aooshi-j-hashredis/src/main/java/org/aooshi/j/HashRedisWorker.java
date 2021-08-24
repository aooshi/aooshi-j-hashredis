package org.aooshi.j;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class HashRedisWorker extends JedisPool {

    private HashRedisConfigItem configItem;

    public HashRedisWorker(HashRedisConfig redisConfig, HashRedisConfigItem configItem) {
        //JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database)
        super(
                redisConfig.getPool()
                , configItem.getHost()
                , configItem.getPort()
                , redisConfig.getConnectTimeout()
                , configItem.getPassword()
                , configItem.getDatabase()
        );

        this.configItem = configItem;
    }

    /**
     * 支持一致性哈希
     *
     * @return
     */
    @Override
    public String toString() {
        String host = this.configItem.getHost();
        int port = this.configItem.getPort();
        int database = this.configItem.getDatabase();

        return String.format("redis://%s:%d/%d", host, port, database);
    }
}
