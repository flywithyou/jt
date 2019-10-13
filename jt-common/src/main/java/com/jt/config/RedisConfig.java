package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

//标识配置类
@Configuration
@PropertySource("classpath:/properties/redis.properties")	//引入配置文件
public class RedisConfig {

	@Value("${redis.shardNodes}")
	private String shardNodes;
	@Value("${redis.sentinelNodes}")
	private String sentinelNodes;
	@Value("${redis.clusterNodes}")
	private String clusterNodes;
	
	/**
	 * 分片
	 * @return
	 */
	@Bean
	public ShardedJedis shardedJedis() {
		List<JedisShardInfo> list = getList();
		return new ShardedJedis(list);
	}

	private List<JedisShardInfo> getList() {
		List<JedisShardInfo> list = new ArrayList<>();
		String[] arrayNodes = shardNodes.split(",");
		for (String node : arrayNodes) {
			String host = node.split(":")[0];
			int port = Integer.valueOf(node.split(":")[1]);
			list.add(new JedisShardInfo(host,port));
		}
		return list;
	}

	//	@Value("${redis.host}")
	//	private String host;
	//	@Value("${redis.port}")
	//	private Integer port;
	/**
	 * 哨兵
	 * @param jedisSentinelPool
	 * @return
	 */
//	@Bean
//	@Scope("prototype")	//多例
//	public Jedis jedis(JedisSentinelPool jedisSentinelPool) {
//		return jedisSentinelPool.getResource();
//	}
//
//	@Bean 	//单例
//	public JedisSentinelPool jedisSentinelPool() {
//		Set<String> sentinels = new HashSet<>();
//		sentinels.add(sentinelNodes);
//		return new JedisSentinelPool("mymaster", sentinels);
//	}
	
	/**
	 * 整合集群
	 * @return
	 */
	@Bean
	public JedisCluster jedisCluster() {
		Set<HostAndPort> set = new HashSet<>();
		String[] arrayNodes = clusterNodes.split(",");
		for (String node : arrayNodes) {
			String host = node.split(":")[0];
			int port = Integer.valueOf(node.split(":")[1]);
			set.add(new HostAndPort(host,port));
		}
		System.out.println(set);
		return new JedisCluster(set);
	}
}
