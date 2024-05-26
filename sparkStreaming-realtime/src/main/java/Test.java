import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Test {

    private static final JedisPool jedisPool;

    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();//配置连接池
        poolConfig.setMaxTotal(8);// 最大连接•
        poolConfig.setMaxIdle(8);// 最大空闲连接
        poolConfig.setMinIdle(0);// 最小空闲连接
        poolConfig.setMaxWaitMillis(200); // 设置最长等待时间， ms
        jedisPool = new JedisPool(poolConfig, "192.168.31.59", 6379, 1000,"123456"); //创建连接池对象
    }

    public static Jedis getJedis() { //获取Jedis对象
        return jedisPool.getResource();
    }

    public static void main(String[] args) {
        Jedis jedis = getJedis();
        jedis.set("name", "小花");
        String name = jedis.get("name");
        System.out.println("name = " + name);
    }
}
