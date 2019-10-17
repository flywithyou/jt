package com.jt.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

@Service
public class DubboUserServiceImpl implements DubboUserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisCluster jedis;
	@Override
	public void saveUser(User user) {
		//防止email为null报错，使用电话代替
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setEmail(user.getPhone()).setCreated(new Date()).setUpdated(user.getCreated());
		user.setPassword(md5Pass);
		userMapper.insert(user);
		
		//将信息加密在存入数据库
	}

	/**
	 * 1.根据用户名和密码查询数据库
	 * 	结果：没有记录 说明用户名和密码不正确 return null
	 * 2.生成ticket（加密后的秘钥），userJSON串，将数据保存到Redis中
	 * 3。返回ticket。
	 * 
	 */
	@Override
	public String doLogin(User user) {
		User userDB = findUserByUP(user);
		if (null != userDB) {
			//1.生成秘钥
			String uuid = UUID.randomUUID().toString();
			String ticket = DigestUtils.md5DigestAsHex(uuid.getBytes());
			//2将某些敏感数据进行脱敏处理
			userDB.setPassword("********");
			//2.将user对象转成json串
			String value = ObjectMapperUtil.toJSON(userDB);
			//将ticket保存到Redis中
			jedis.setex(ticket, 7*24*3600, value);
			return ticket;
		}
		return null;
	}

	public User findUserByUP(User user) {
		String mdrPass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(mdrPass);
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
		User userDB = userMapper.selectOne(queryWrapper);
		return userDB;
	}
}
