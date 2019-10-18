package com.jt.util;

import com.jt.pojo.User;

public class ThreadLocalUtil {
	private static ThreadLocal<User> threadLocal = new ThreadLocal<>();
	public static void setUser(User user) {
		threadLocal.set(user);
	}
	public static User get() {
		return threadLocal.get();
	}
	
	//注意内存泄漏问题
	public static void remove() {
		threadLocal.remove();
	}
}
