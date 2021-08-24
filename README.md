# consistent hashing redis client

the project is a consistent hashing redis client  
这是一个一致性哈希redis客户端

## use to you application

use project your application  
应用项目到你的应用程序

### step 1. import package

pom.xml
```xml
<dependency>
    <groupId>org.aooshi.j</groupId>
    <artifactId>aooshi-j-hashredis</artifactId>
    <version>1.0.0</version>
</dependency>
```

### step 2. configuration application.yml

```yaml
hashredis:
  # 节点检查时间周期（秒）
  # node check period (second)
  check-period: 5
  # redis server list
  servers:
  - database: 10
    host: 172.20.1.7
    port: 6379
    # password: 123456
  - database: 11
    host: 172.20.1.7
    port: 6379
    # password: 123456
  - database: 12
    host: 172.20.1.7
    port: 6379
    # password: 123456

#  # 是否启用节点初始打印
#  # node initial print switch
#  enable-print: 0

#  #pool config
#  pool:
#    # 资源池中的最大连接数,默认: 1000
#    max-total: 1000
#    # 资源池允许的最大空闲连接数,默认： 16
#    max-idle: 16
#    # 资源池确保的最少空闲连接数:默认：1
#    min-idle: 1
#    # 当资源池用尽后，调用者是否要等待。
#    # 只有当值为true时，maxWaitMillis才会生效。 默认：true
#    blockWhenExhausted: true
#    # 当资源池连接用尽后，调用者的最大等待时间（单位为毫秒）。
#    # 默认：10000
#    maxWaitMillis: 10000
#    # 向资源池借用连接时是否做连接有效性检测（ping）。
#    # 检测到的无效连接将会被移除。默认： false
#    testOnBorrow: false
#    # 向资源池归还连接时是否做连接有效性检测（ping）。
#    # 检测到无效连接将会被移除。默认： false
#    testOnReturn: false
#    # 是否开启JMX监控,默认：true
#    jmxEnabled: true

```

### step 3. configuration bean

```java
import org.aooshi.j.HashRedisConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HashRedisProperties {

    @Bean
    @ConfigurationProperties(prefix = "hashredis")
    public HashRedisConfig hashRedisConfig() {
        return new HashRedisConfig();
    }

}

```
### a example

```java
public class HashRedisTest {

    @Autowired
    private HashRedisConfig hashRedisConfig;

    @Test
    public void RedisHashingTest() {

        //data preparation
        String key = "test-user1";
        String value = new Date().toString();
        String result = "";

        //use
        HashRedis redis = new HashRedis(this.hashRedisConfig);
        redis.getNode(key).set(key, value);
        result = redis.getNode(key).get(key);

        //comparison
        Assert.assertEquals(value, result);

    }
}
```
