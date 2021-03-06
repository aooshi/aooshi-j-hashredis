package org.aooshi.j;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 一致性哈希
 */
@Slf4j
public class HashRedis {

    private HashRedisConfig redisConfig;

    private volatile HashRedisConfigItem[] cfg_configItems;
    private volatile HashRedisConfigItem[] configItems;
    private volatile String configTag = "";

    private ConsistentHashing<JedisPool> hashing;

    public HashRedis(HashRedisConfig hashRedisConfig) {
        this.redisConfig = hashRedisConfig;
        this.configItems = this.redisConfig.getServers().toArray(new HashRedisConfigItem[0]);
        this.cfg_configItems = this.redisConfig.getServers().toArray(new HashRedisConfigItem[0]);
        this.configTag = this.buildCheckTag(this.redisConfig.getServers());
        this.reset();
        //
        this.monitor();
    }

    private void monitor() {
        Integer checkPeriod = this.redisConfig.getCheckPeriod();
        if (checkPeriod < 1) return;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(checkPeriod * 1000);
                    } catch (InterruptedException e) {

                    }
                    serverCheck();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private synchronized void serverCheck() {
        List<HashRedisConfigItem> list = new ArrayList<>();
        for (HashRedisConfigItem item : this.cfg_configItems) {
            boolean success = this.check(item);
            if (success == true) {
                list.add(item);
            } else {
                log.info(String.format("redis://%s:%d/%d check failure.", item.getHost(), item.getPort(), item.getDatabase()));
            }
        }
        String newTag = this.buildCheckTag(list);
        //changed
        if (newTag.equals(this.configTag) == false) {
            this.configTag = newTag;
            this.configItems = list.toArray(new HashRedisConfigItem[0]);
            this.reset();
        }
    }

    private String buildCheckTag(List<HashRedisConfigItem> items) {
        StringBuffer builder = new StringBuffer();
        for (HashRedisConfigItem item : items) {
            builder.append(item.getHost());
            builder.append(item.getPort());
            builder.append(item.getDatabase());
        }
        return builder.toString();
    }

    private boolean check(HashRedisConfigItem item) {
        boolean deleted = false;
        Jedis jedis = null;
        try {
            jedis = new Jedis(item.getHost(), item.getPort(), 1500, 1000);
            if (item.getPassword() != null && item.getPassword() != "") {
                jedis.auth(item.getPassword());
            }
            String r = jedis.ping();
            deleted = "PONG".equals(r);
        } catch (Exception e) {
            deleted = false;
        } finally {
            if (jedis != null) {
                try {
                    jedis.close();
                } catch (Exception e2) {
                }
            }
        }

        if (deleted == false) {
            log.info("ping " + item.getHost() + ":" + item.getPort() + " failure.");
        }

        return deleted;
    }

    private void reset() {
        this.hashing = new ConsistentHashing<>(new JedisPool[0]);
        JedisPool[] nodes = this.createNodes();
        if (nodes == null || nodes.length == 0) {
            return;
        }
        this.hashing = new ConsistentHashing<>(nodes);
        if (this.redisConfig.getEnablePrint() == 1) {
            this.hashing.printNodes();
        }
    }

    private JedisPool[] createNodes() {
        HashRedisConfigItem[] configItems = this.configItems;
        JedisPool[] pools = new JedisPool[configItems.length];
        for (int i = 0; i < configItems.length; i++) {
            pools[i] = new HashRedisWorker(this.redisConfig, configItems[i]);
        }
        return pools;
    }

    /**
     * 依据key获取node
     *
     * @param key
     * @return
     */
    @SneakyThrows
    public Jedis getNode(String key) {
        JedisPool node = this.hashing.getNode(key);
        if (node == null)
            throw new IOException("No cache server is available.");
        //
        Jedis resource = node.getResource();
        if (resource == null)
            throw new IOException("No cache server is available.");
        return resource;
    }
}
