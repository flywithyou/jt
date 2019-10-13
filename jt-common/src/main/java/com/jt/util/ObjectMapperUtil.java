package com.jt.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	/**
	 * 	对象转化为json
	 * @param target	要转化的目标对象
	 * @return			目标对象的json格式
	 */
	public static String toJSON(Object target) {
		String result = null;
		try {
			result = MAPPER.writeValueAsString(target);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}
	/**
	 * json 转化为对象
	 * @param <T>		目标类型
	 * @param json		json串
	 * @param target	目标类型class对象
	 * @return
	 */
	public static <T> T toObject(String json,Class<T> target){
		T object = null;
		try {
			object = MAPPER.readValue(json, target);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}	
		return object;
	}
}
