import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;


public class testJedis {
	@Test
	public void testJedisPool() {
		// 创建一个数据库连接池对象（单例，即一个系统共用一个连接池），需要指定服务的IP和端口号
		JedisPool jedisPool = new JedisPool("122.152.226.53", 6379);
		// 从连接池中获得连接
		Jedis jedis = jedisPool.getResource();
	    jedis.set("jedis-key", "1234");
		// 使用jedis操作数据库（方法级别，就是说只是在该方法中使用，用完就关闭）
		String result = jedis.get("jedis-key");
		System.out.println(result);
		// 用完之后关闭jedis连接
		jedis.close();
		// 系统关闭前先关闭数据库连接池
		jedisPool.close();
	}
	
	@Test
	public void testJedisCluster() throws Exception{
		//创建一个JedisCluster对象，构造参数Set类型，集合中每个元素是HostAndPort
		Set<HostAndPort> nodes=new HashSet<>();
		//向集合中添加节点
		nodes.add(new HostAndPort("122.152.226.53", 7001));
		nodes.add(new HostAndPort("122.152.226.53", 7002));
		nodes.add(new HostAndPort("122.152.226.53", 7003));
		nodes.add(new HostAndPort("122.152.226.53", 7004));
		nodes.add(new HostAndPort("122.152.226.53", 7005));
		nodes.add(new HostAndPort("122.152.226.53", 7006));
		JedisCluster jedisCluster=new JedisCluster(nodes);
		//直接使用JedisCluster操作redis，自带连接池。jedisCluster可以使单例的
		jedisCluster.set("clusyer-test", "hello Jedis Cluster");
		String string =jedisCluster.get("clusyer-test");
		System.out.println(string);
		//系统关闭前关闭JedisCluster(默认单利)
		jedisCluster.close();
		
		
		
		
	}

}