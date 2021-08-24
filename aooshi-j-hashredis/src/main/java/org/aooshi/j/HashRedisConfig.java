package org.aooshi.j;

import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * hash redis config
 */
public class HashRedisConfig {

    private List<HashRedisConfigItem> servers = new ArrayList<>();
    private Integer checkPeriod = 0;
    private Integer enablePrint = 0;
    private Integer connectTimeout = 5000;
    private JedisPoolConfig pool = createDefaultPoolConfig();

    public List<HashRedisConfigItem> getServers() {
        return servers;
    }

    public Integer getCheckPeriod() {
        return checkPeriod;
    }

    /**
     * set check period
     *
     * @param checkPeriod 0 / 1
     */
    public void setCheckPeriod(Integer checkPeriod) {
        this.checkPeriod = checkPeriod;
    }

    public Integer getEnablePrint() {
        return enablePrint;
    }

    /**
     * set enable print,
     *
     * @param enablePrint 0 / 1
     */
    public void setEnablePrint(Integer enablePrint) {
        this.enablePrint = enablePrint;
    }

    /**
     * get connect timeout
     *
     * @return
     */
    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * set connect timeout
     *
     * @param connectTimeout
     */
    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * get pool config
     *
     * @return
     */
    public JedisPoolConfig getPool() {
        return pool;
    }

    /**
     * set jedis pool config
     *
     * @param pool
     */
    public void setPool(JedisPoolConfig pool) {
        this.pool = pool;
    }


    private static JedisPoolConfig createDefaultPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(1000);
        config.setMaxIdle(16);
        config.setMinIdle(1);
        config.setMaxWaitMillis(10000);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        config.setJmxEnabled(true);
        return config;
    }
}
