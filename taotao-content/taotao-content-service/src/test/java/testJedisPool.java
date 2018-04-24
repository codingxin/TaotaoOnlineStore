import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class testJedisPool {
	@Test
	public void testJedisPool1() {
		// 创建一个数据库连接池对象（单例，即一个系统共用一个连接池），需要指定服务的IP和端口号
		JedisPool jedisPool = new JedisPool("122.152.226.53", 6379);
		// 从连接池中获得连接
		Jedis jedis = jedisPool.getResource();
		// 使用jedis操作数据库（方法级别，就是说只是在该方法中使用，用完就关闭）
		String result = jedis.get("jedis-key");
		System.out.println(result);
		// 用完之后关闭jedis连接
		jedis.close();
		// 系统关闭前先关闭数据库连接池
		jedisPool.close();
	}

}