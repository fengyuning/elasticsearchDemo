package com.pirate.esredisdemo.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JedisTest {
    @Autowired
    private JedisPool jedisPool;

    //redis有多种数据类型

    @Test
    public void stringTest() {
        Jedis jedis = jedisPool.getResource();

        String key = "猪猪溪";
        String set = jedis.set(key, "来自武汉");
        String get = jedis.get(key);
        Long append = jedis.append(key, "111");
        String mset = jedis.mset("age", "25", "year", "2018");
        Long age = jedis.incr("age");
        Long year = jedis.incrBy("year", 10);
        jedis.del("age");
        System.err.println("string 的基本用法");
    }

    @Test
    public void listTest(){
        Jedis jedis = jedisPool.getResource();

        String key = "testList";
        jedis.lpush(key,"111");
        jedis.lpushx(key,"222");
        jedis.lpush(key,"333");
        String lpop = jedis.lpop(key);
        Long lpushx = jedis.lpushx(key, "1");

        String lindex = jedis.lindex(key, 2);
        List<String> lrange = jedis.lrange(key, 0, -1);//取出所有

        jedis.del(key);
        jedis.lpush(key,"aaa");
        jedis.lpush(key,"bbb");
        jedis.lpush(key,"ccc");
        jedis.lrange(key,0,-1);
    }

    @Test
    public void hashTest(){
        Jedis jedis = jedisPool.getResource();

        String key = "testHash";
        jedis.hset(key,"猪猪溪","111");
        Map<String, String> map = new HashMap<>();
        map.put("a","222");
        map.put("b","333");
        jedis.hmset(key,map);

        Set<String> hkeys = jedis.hkeys(key);
        jedis.hincrBy(key,"a",10);

    }
}
