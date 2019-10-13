package com.jt.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)			//注解修饰的范围
@Retention(RetentionPolicy.RUNTIME)	//作用范围
public @interface Cache_Find {
	String key() default "";	//用户可自定义key，null自动生成key，!null使用指定的key
	int second() default 0; 	//用户可自定义缓存的数据的期限，0表示不过期
}
