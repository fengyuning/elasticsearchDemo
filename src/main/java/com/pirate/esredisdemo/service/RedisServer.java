//package com.pirate.esredisdemo.service;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//
//
//@Service
//public class RedisServer {
//    private static final Logger logger = LoggerFactory.getLogger(RedisServer.class);
//
//    @Autowired
//    private JedisPool jedisPool;
//
//    public void set(String key, String value) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            jedis.set(key, value);
//        } catch (Exception e) {
//            logger.error("ee");
//        } finally {
//            jedis.close();
//        }
//    }
//
//}
