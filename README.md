# consistent hashing redis client

the project is a consistent hashing redis client  
这是一个一致性哈希redis客户端

## use to you application

use project your application  
应用项目到你的应用程序

### setp 1. import package

pom.xml
```xml
<dependency>
    <groupId>org.aooshi.j</groupId>
    <artifactId>aooshi-j-hashredis</artifactId>
    <version>1.0.0</version>
</dependency>
```

### setp 2. configuration application.yml

```yaml
hashredis:
  # 是否启用节点初始打印
  # node initial print switch
  enable-print: 0
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

```

### setp 3. configuration bean

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
