package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	@Reference(check = false)
	private DubboUserService dubboUserSerivce;
	@Autowired
	private JedisCluster jedisCluster;
	
	private static final String TICKET = "JT_TICKET";
	@RequestMapping("/{moduleName}")
	public String module(@PathVariable String moduleName) {
		return moduleName;
	}
	
	@ResponseBody
	@RequestMapping("/doRegister")
	public SysResult saveUser(User user) {
		dubboUserSerivce.saveUser(user);
		return SysResult.Success();
	}
	
	
	@ResponseBody
	@RequestMapping("/doLogin")
	public SysResult doLogin(User user,HttpServletResponse response) {
		String ticket = dubboUserSerivce.doLogin(user);
		if (StringUtils.isEmpty(ticket)) {
			return SysResult.fail("用户名或密码错误！");
		}
		//将ticket保存到客户端的cookie中
		Cookie ticketCookie = new Cookie(TICKET, ticket);
		//设置cookie的生命周期
		ticketCookie.setMaxAge(7*24*3600);
		//cookie的权限的设定，"/"表示在根路径下
		ticketCookie.setPath("/");
		ticketCookie.setDomain("jt.com");
		//将cookie写到客户端
		response.addCookie(ticketCookie);
		return SysResult.Success(ticket);
	}
	
	/**
	 * 0.获取JT_TICKET 的cookie的值 ticket
	 * 1.删除cookie 	名称为JT_TICKET
	 * 2.删除Redis	根据tick值
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		String ticket = null;
		if (cookies.length != 0) {
			for (Cookie cookie : cookies) {
				//获取指定的cookie
				if (cookie.getName().equals("JT_TICKET")) {
					ticket = cookie.getValue();
					break;					
				}
			}
		}
		if (ticket != null) {
			//从缓存中删除cookie信息
			jedisCluster.del(ticket);
			//删除cookie(新建一个键名相同的cookie，并设置maxage为0，覆盖原来的）
			Cookie cookie = new Cookie("JT_TICKET", "");
			cookie.setMaxAge(0);
			cookie.setPath("/");
			cookie.setDomain("jt.com");
			response.addCookie(cookie);
		}
		return "redirect:/";
	}
}
