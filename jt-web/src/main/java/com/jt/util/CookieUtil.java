package com.jt.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
	
	/**
	 *	 编辑工具API方法，通过cookie的名称，获取cookie的值
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request,String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length != 0) {
			for (Cookie cookie : cookies) {
				if (cookieName.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}			
		}
		//如果没有cookie，则直接返回null
		return null;
	}
	public  static void addCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,String cookieValue,int seconds,String domain) {
		//将ticket保存到客户端的cookie中
		Cookie ticketCookie = new Cookie(cookieName, cookieValue);
		//设置cookie的生命周期
		ticketCookie.setMaxAge(seconds);
		//cookie的权限的设定，"/"表示在根路径下
		ticketCookie.setPath("/");
		ticketCookie.setDomain(domain);
		//将cookie写到客户端
		response.addCookie(ticketCookie);
	}
}
