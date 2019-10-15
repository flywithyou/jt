package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.annotion.Cache_Find;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

@Aspect		//标识切面
@Component	//该类交由spring容器管理
public class RedisAspect {
	//当前切面位于common中，必须添加required=false
	@Autowired(required = false)
	private JedisCluster jedis;		//集群机制
//	private Jedis jedis;			//哨兵机制
//	private ShardedJedis jedis;		//分片机制	
//	private Jedis jedis;
	
	//	@Pointcut("@annotation(com.jt.annotion.Cache_Find)")
	//	public void itemCatPointCut() {}

	/**
	 * 通知选择:
	 * 	是否需要控制目标方法执行,使用环绕通知
	 * 步骤:
	 * 	1.动态生成key  包名.类名.方法名::parentId
	 *  2.通过key查询redis缓存
	 * @param jp
	 * @param cacheFind
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(cacheFind)")	//直接获取注解对象
	public Object around(ProceedingJoinPoint jp,Cache_Find cacheFind) {
		//获取目标方法的所有参数
		String key = getKey(jp, cacheFind);
		//先从Redis缓存中根据parentId获取值
		String json = jedis.get(key);
		Object data  = null;
		//假如值为null，则说明缓存中还没有存储有响应的数据，需要从数据库中查询
		try {
			if (StringUtils.isEmpty(json)) {
				//执行目标方法,从数据库中根据parentId查询
				data = jp.proceed();
				//将查询到的对象转化为json串，并存入Redis缓存中
				json = ObjectMapperUtil.toJSON(data);
				//判断用户是否设置了超时时间
				int seconds = cacheFind.second();
				if (seconds == 0) {
					jedis.set(key, json);
				}else {
					jedis.setex(key, seconds, json);
				}
				System.out.println("AOP数据库查询");
			}else {
				//获取目标方法的返回值类型
				Class returnClass = getReturnClass(jp);
				data =  ObjectMapperUtil.toObject(json, returnClass);
				System.out.println("AOP缓存查询");
			}			
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return data;
	}
	
	private Class getReturnClass(ProceedingJoinPoint jp) {
		MethodSignature singnature = (MethodSignature) jp.getSignature();
		return singnature.getReturnType();
	}

	/**
	 * 生成key
	 * @param jp
	 * @param cacheFind
	 * @return
	 */
	private String getKey(ProceedingJoinPoint jp,Cache_Find cacheFind) {
		//1.判断用户是否定义了key值
		String key = cacheFind.key();
		if (!StringUtils.isEmpty(key)) {
			return key;		//返回用户自定义的key
		}
		Object[] args = jp.getArgs();
		String className = jp.getSignature().getDeclaringTypeName();	//获取包名+类名
		String methodName = jp.getSignature().getName();
		key = className+"."+methodName+"::"+args[0];
		return key;
	}
}
