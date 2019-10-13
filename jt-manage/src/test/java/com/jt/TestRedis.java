package com.jt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Transaction;

public class TestRedis {
	private Jedis jedis;
	@Before
	public void init() {
		jedis = new Jedis("192.168.88.132", 6379);
	}
	/**
	 * 1.测试String
	 * @throws Exception
	 */
	@Test
	public void testString() throws Exception {
		jedis.set("1906", "redis入门案例");
		String value = jedis.get("1906");
		System.out.println(value);
		
		//2.测试key相同时value是否覆盖,值被覆盖
		jedis.set("1906", "redis测试");
		value = jedis.get("1906");
		System.out.println(value);
		
		//3.如果值已经存在 则不允许覆盖
		jedis.setnx("1906", "NBA不转播了");
		System.out.println(jedis.get("1906"));
		
		//4.为数据添加超时时间
		jedis.set("time", "超时测试");
		jedis.expire("time", 60);
		//保证数据操作有效性（原子性）
		jedis.setex("time", 100, "超时测试");
		Thread.sleep(3000);
		Long time = jedis.ttl("time");
		System.out.println("当前数据剩余存活时间："+time);
		
		//5.要求key存在时不允许操作，并且设定超时时间
		//nx:不允许覆盖	xx：允许覆盖
		//ex：单位秒		px：单位毫秒
		jedis.set("时间", "测试是否有效", "NX", "EX", 100);
		time = jedis.ttl("时间");
		System.out.println("当前数据剩余存活时间："+time);
	}
	/**
	 * 2.测试hash
	 */
	@Test
	public void testHash() {
		jedis.hset("person", "id", "100");
		jedis.hset("person", "name", "超人");
		Map<String, String> person = jedis.hgetAll("person");
		System.out.println(person);
	}
	/**
	 * 3.测试list集合
	 * 	队列
	 */
	@Test
	public void testList() {
		jedis.rpush("list", "1,2,3,4");
		System.out.println(jedis.lpop("list"));
		jedis.rpush("list", "1","2","3","4");
		System.out.println(jedis.lpop("list"));
		jedis.lpush("list", "1","2","3","4");
		System.out.println(jedis.lpop("list"));
//		System.out.println(jedis.lpop("list"));
//		System.out.println(jedis.lpop("list"));
	}
	
	/**
	 * 4.控制事务
	 * 
	 */
	@Test
	public void testTx() {
		//开启事务
		Transaction transaction = jedis.multi();
		try {
			transaction.set("a", "aa");
			transaction.set("b", "bb");
			transaction.set("c", "cc");
			int a = 1/0;
			//2.事务提交
			transaction.exec();
		} catch (Exception e) {
			e.printStackTrace();
			//3.事务回滚
			transaction.discard();
		}
	}
	
	/**
	 * 实现Redis分片操作
	 */
	@Test
	public void testShards() {
		List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
		list.add(new JedisShardInfo("192.168.88.132", 6379));
		list.add(new JedisShardInfo("192.168.88.132", 6381));
		list.add(new JedisShardInfo("192.168.88.132", 6380));
		ShardedJedis jedis  = new ShardedJedis(list);
		jedis.set("1906", "redis分片测试");
		System.out.println(jedis.get("1906"));
	}
	
	/**
	 * 测试哨兵
	 * 调用原理
	 * 	用户通过哨兵，连接Redis主机，进行操作
	 * mysterName：主机的变量名
	 * sentinel：Redis节点信息 set(String)
	 */
	@Test
	public void testSentinel() {
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.88.132:26379");
		JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels);
		Jedis jedis = sentinelPool.getResource();
		jedis.set("a", "100");
		System.out.println(jedis.get("a"));
	}
	
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 测试集群
	 */
	@Test
	public void testCluster() {
//		Set<HostAndPort> nodes = new HashSet<>();
//		nodes.add(new HostAndPort("192.168.88.132",7000));
//		nodes.add(new HostAndPort("192.168.88.132", 7001));
//		nodes.add(new HostAndPort("192.168.88.132", 7002));
//		nodes.add(new HostAndPort("192.168.88.132", 7003));
//		nodes.add(new HostAndPort("192.168.88.132", 7004));
//		nodes.add(new HostAndPort("192.168.88.132", 7005));
//		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("1906", "测试集群成功");
		System.out.println(jedisCluster.get("1906"));
	}
}
