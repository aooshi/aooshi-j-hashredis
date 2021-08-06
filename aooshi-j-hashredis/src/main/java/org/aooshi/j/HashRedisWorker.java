package org.aooshi.j;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class HashRedisWorker extends JedisPool {

    private static int timeout = 5000;

    // JedisConfig attribute
    private static int max_active = 1024;
    private static int max_idle = 200;
    private static int max_wait = 10000;
    private static boolean test_on_borrow = true;

    private HashRedisConfigItem configItem;

    public HashRedisWorker(HashRedisConfigItem configItem) {
        //JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database)
        super(
                createPoolConfig()
                , configItem.getHost()
                , configItem.getPort()
                , timeout
                , configItem.getPassword()
                , configItem.getDatabase()
        );

        this.configItem = configItem;
    }

    private static JedisPoolConfig createPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(max_idle);
        config.setMaxWaitMillis(max_wait);
        config.setTestOnBorrow(test_on_borrow);
        config.setMaxTotal(max_active);
        return config;
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
