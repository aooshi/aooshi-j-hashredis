package org.aooshi.j;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * redis config
 */
@Component
@ConfigurationProperties(prefix = "hashredis")
public class HashRedisConfig {

    private List<HashRedisConfigItem> servers;
    private Integer checkPeriod = 0;

    public List<HashRedisConfigItem> getServers() {
        return servers;
    }

    public void setServers(List<HashRedisConfigItem> servers) {
        this.servers = servers;
    }

    public Integer getCheckPeriod() {
        return checkPeriod;
    }

    public void setCheckPeriod(Integer checkPeriod) {
        this.checkPeriod = checkPeriod;
    }
}
