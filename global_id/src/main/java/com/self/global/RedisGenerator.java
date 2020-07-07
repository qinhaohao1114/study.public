package com.self.global;

import redis.clients.jedis.Jedis;

import static java.lang.System.*;

public class RedisGenerator {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        Long id = jedis.incr("id");
        out.println(id);
    }
}
