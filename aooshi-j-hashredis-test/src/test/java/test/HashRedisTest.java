package test;

import org.aooshi.j.HashRedis;
import org.aooshi.j.HashRedisConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HashRedisTestApplication.class)
public class HashRedisTest {

    @Autowired
    private HashRedisConfig hashRedisConfig;

    @Test
    public void RedisHashingTest() {

        HashRedis redis = new HashRedis(this.hashRedisConfig);

        String key = "test-user1";
        String value = new Date().toString();

        redis.getNode(key).set(key, value);

        String result = redis.getNode(key).get(key);

        Assert.assertEquals(value, result);

    }
}
