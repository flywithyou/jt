package com.jt.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.ThreadLocalUtil;

import redis.clients.jedis.JedisCluster;

@Component
public class UserInterceptor implements HandlerInterceptor{
	@Autowired
	private JedisCluster jedisCluster;
//	private static final String JTUSER ="JT-USER";
	/**
	 *	 方法执行之前执行
	 *	return true 表示放行，false表示拦截
	 *	用户未登录，不允许访问，即返回false
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String ticket = CookieUtil.getCookieValue(request, "JT_TICKET");
		String username = CookieUtil.getCookieValue(request, "JT_USERNAME");
		String localTicket = jedisCluster.hget(username, "JT_TICKET");
		if (ticket.equalsIgnoreCase(localTicket)) {
			String userJSON = jedisCluster.hget(username,"JT_USERJSON");
			if (!StringUtils.isEmpty(userJSON)) {
				User user = ObjectMapperUtil.toObject(userJSON, User.class);
//				request.setAttribute("JTUSER",user);
				System.err.println(user);
				ThreadLocalUtil.setUser(user);
				return true;
			}
		}
		//一般拦截器中的false和重定向联用
		//应该重定向到登录页面
		response.sendRedirect("/user/login.html");
		return false;
	}
	/**
	 * 	方法执行完之后执行
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("方法执行完之后执行");
	}
	
	/**
	 * 	视图渲染完成之后执行
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		request.removeAttribute("JTUSER");
	}
}
