package org.aooshi.j;

import java.util.ArrayList;
import java.util.List;

/**
 * hash redis config
 */
public class HashRedisConfig {

    private List<HashRedisConfigItem> servers = new ArrayList<>();
    private Integer checkPeriod = 0;
    private Integer enablePrint = 0;

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
}
