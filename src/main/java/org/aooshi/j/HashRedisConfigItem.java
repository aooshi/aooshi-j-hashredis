package org.aooshi.j;

import lombok.Data;

@Data
public class HashRedisConfigItem {
    private Integer database = 0;
    private String host = null;
    private Integer port = 0;
    private String password = null;
}
