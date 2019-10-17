package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.service.UserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedis;
	
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param, @PathVariable Integer type,String callback) {
		boolean data = userService.checkUsere(param,type);
		System.err.println(data);
		return new JSONPObject(callback, SysResult.Success(data));
	}
	@RequestMapping("/query/{ticket}")
	public JSONPObject querykUser(@PathVariable String ticket, String callback) {
		String userJSON = jedis.get(ticket);
		if (StringUtils.isEmpty(userJSON)) {
			//用户使用的ticket有问题
			return new JSONPObject(callback, SysResult.fail());
		}
		System.err.println(userJSON);
		return new JSONPObject(callback, SysResult.Success(userJSON));
	}
}
