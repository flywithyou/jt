package com.jt.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.service.UserService;
import com.jt.util.IPUtil;
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
	@RequestMapping("/query/{ticket}/{username}")
	public JSONPObject querykUser(@PathVariable String ticket, @PathVariable String username,String callback,HttpServletRequest request) {
		String ip = IPUtil.getIpAddr(request);
		String localIP = jedis.hget(username, "JT_IP");
		//检验用户IP地址
		if (!ip.equalsIgnoreCase(localIP)) {
			return new JSONPObject(callback, SysResult.fail());
		}
		//2.检验ticket信息
		String localTicket = jedis.hget(username,"JT_TICKET");
		if (!ticket.equalsIgnoreCase(localTicket)) {
			return new JSONPObject(callback,SysResult.fail());
		}
		//3.说明用户信息正确
		String userJSON = jedis.hget(username, "JT_USERJSON");
		return new JSONPObject(callback, SysResult.Success(userJSON));
	}
}
