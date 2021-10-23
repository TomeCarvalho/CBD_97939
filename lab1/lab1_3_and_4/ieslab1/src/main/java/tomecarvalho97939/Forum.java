package tomecarvalho97939;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.*;
import java.util.Arrays;
import redis.clients.jedis.Jedis;

public class Forum {
    private Jedis jedis;
    public Forum() {
        this.jedis = new Jedis("localhost");
        System.out.println(jedis.info());
        jedis.set("1", "one");
        jedis.set("2", "two");
        jedis.del("2");
        jedis.append("1", ", um");
        System.out.println(jedis.get("1"));
        jedis.expire("1", 10);
        jedis.ttl("1");
        jedis.persist("1");
        jedis.lpush("hw", "hello");
        jedis.rpush("hw", "world");
        System.out.println(jedis.lrange("hw", 0, -1));
        jedis.lpop("hw");
        Set<String> dbd = new HashSet<>(
            Arrays.asList("Trapper", "Wraith", "Hillbilly", "Nurse", "Trickster")
        );
        for (String killer : dbd)
            jedis.sadd("dbd", killer);
        jedis.srem("dbd", "Trickster");
        System.out.println(jedis.smembers("dbd"));
    }
    public static void main(String[] args) {
        new Forum();
    }
}