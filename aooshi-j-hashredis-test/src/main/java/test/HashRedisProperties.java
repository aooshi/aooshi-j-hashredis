package test;

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
